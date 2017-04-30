package com.zcorp.api;

import org.skife.jdbi.v2.sqlobject.SqlQuery;

public interface HealthDao {

    @SqlQuery("select 1")
    Integer ping();

}
