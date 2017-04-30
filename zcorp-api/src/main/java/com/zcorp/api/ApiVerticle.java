package com.zcorp.api;

import com.zaxxer.hikari.HikariDataSource;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.ext.web.Router;
import org.skife.jdbi.v2.DBI;
import rx.Emitter;
import rx.Observable;

import javax.sql.DataSource;

public class ApiVerticle extends AbstractVerticle {

    private DataSource dataSource;
    private VertxScheduler scheduler;

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        this.scheduler = new VertxScheduler(RxHelper.blockingScheduler(vertx),
                                            RxHelper.scheduler(vertx));

        Observable.create(subscriber -> {

            String jdbcUrl = JdbcUrl.mysql(System.getenv("MYSQL_HOST"),
                                           Integer.parseInt(System.getenv("MYSQL_PORT")),
                                           System.getenv("DATABASE_NAME"),
                                           false,
                                           false);

            HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl(jdbcUrl);
            ds.setUsername(System.getenv("MYSQL_USER"));
            ds.setPassword(System.getenv("MYSQL_PASSWORD"));
            this.dataSource = ds;

            listenHttp();
            subscriber.onCompleted();
        }).subscribeOn(this.scheduler.worker())
                  .observeOn(this.scheduler.eventLoop())
                  .subscribe(next -> {
                             },
                             err -> {
                                 System.err.println("Unable to start server ");
                                 err.printStackTrace();
                                 startFuture.fail(err);
                             },
                             startFuture::complete);
    }

    private void listenHttp() {

        System.out.println("Initializing HTTP...");

        Router router = Router.router(vertx);

        router.get("/").handler(ctx -> ctx.response().end(new JsonObject()
                                                              .put("env", System.getenv("API_ENV"))
                                                              .put("now", System.currentTimeMillis())
                                                              .toString()));

        router.get("/healthz").handler(ctx ->
                                           Observable.fromEmitter(emitter -> {
                                               HealthDao healthDao = new DBI(dataSource).onDemand(HealthDao.class);
                                               emitter.onNext(healthDao.ping());
                                               emitter.onCompleted();
                                           }, Emitter.BackpressureMode.BUFFER)
                                                     .subscribeOn(scheduler.worker())
                                                     .observeOn(scheduler.eventLoop())
                                                     .subscribe(integer -> ctx.response()
                                                                              .end(new JsonObject().put("database", integer)
                                                                                                   .toString()),
                                                                ctx::fail, () -> {
                                                         }));

        HttpServerOptions options = new HttpServerOptions();
        options.setCompressionSupported(true);

        HttpServer server = vertx.createHttpServer(options);
        server.requestHandler(router::accept).listen(3000, "0.0.0.0");
    }
}
