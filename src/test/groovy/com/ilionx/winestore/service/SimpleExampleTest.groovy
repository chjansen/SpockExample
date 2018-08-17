package com.ilionx.winestore.service

import spock.lang.Specification
import spock.lang.Unroll


class SimpleExampleTest extends Specification{

    def "should return 2 from first element of list"() {
        given:
        List<Integer> list = new ArrayList<>()
        when:
        list.add(2)
        then:
        2 == list.get(0)
    }

    @Unroll
    def "numbers to the power of two"() {
        expect:
        Math.pow(a, b) == c

        where:
        a | b | c
        1 | 2 | 1
        2 | 2 | 4
        3 | 2 | 9
    }

    def "should return 2 for method parameter equal to 2 and 1 on the second call"() {
        given:
        List list = Stub()
        list.get(0) >> 0
        list.get(1) >> { throw new IllegalArgumentException() }
        list.get(2) >>> [2,1]

        when:
        def result = list.get(2)
        then:
        result == 2

        when:
        result = list.get(2)
        then:
        result == 1

        when:
        list.get(1)
        then:
        thrown(IllegalArgumentException)
    }
}
