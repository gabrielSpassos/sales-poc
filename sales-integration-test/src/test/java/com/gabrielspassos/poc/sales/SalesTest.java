package com.gabrielspassos.poc.sales;

import com.intuit.karate.junit5.Karate;

public class SalesTest {

    @Karate.Test
    Karate testVoteScenarios() {
        return Karate.run("sales").relativeTo(getClass());
    }

}
