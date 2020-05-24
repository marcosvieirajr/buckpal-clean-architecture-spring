package br.com.mvj.buckpal.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MoneyTest {

    @Test
    void testMoney() {

        assertThat(Money.add(Money.of(10L), Money.of(10L))).isEqualTo(Money.of(20L));

        assertThat(Money.subtract(Money.of(15L), Money.of(10L))).isEqualTo(Money.of(5L));
        assertThat(Money.subtract(Money.of(5L), Money.of(10L))).isEqualTo(Money.of(-5L));
        assertThat(Money.subtract(Money.of(5L), Money.of(10L))).isEqualTo(Money.of(-5L));

        assertThat(Money.of(10L).isPositive()).isTrue();
        assertThat(Money.of(-10L).isPositive()).isFalse();

        assertThat(Money.of(-10L).isPositiveOrZero()).isFalse();
        assertThat(Money.of(0L).isPositiveOrZero()).isTrue();
        assertThat(Money.of(10L).isPositiveOrZero()).isTrue();

        assertThat(Money.of(10L).isNegative()).isFalse();
        assertThat(Money.of(-10L).isNegative()).isTrue();
        assertThat(Money.of(0L).isNegative()).isFalse();

        assertThat(Money.of(10L).isGreaterThanOrEqualTo(Money.of(10L))).isTrue();
        assertThat(Money.of(15L).isGreaterThanOrEqualTo(Money.of(10L))).isTrue();
        assertThat(Money.of(5L).isGreaterThanOrEqualTo(Money.of(10L))).isFalse();

        assertThat(Money.of(5L).isGreaterThan(Money.of(10L))).isFalse();
        assertThat(Money.of(15L).isGreaterThan(Money.of(10L))).isTrue();

        assertThat(Money.of(15L).minus(Money.of(10L))).isEqualTo(Money.of(5L));

        assertThat(Money.of(15L).plus(Money.of(10L))).isEqualTo(Money.of(25L));

        assertThat(Money.of(15L).negate()).isEqualTo(Money.of(-15L));
    }

}
