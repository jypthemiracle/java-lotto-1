package lotto.domain;

public class Money {
    private static final int MONEY_PER_LOTTO = 1_000;
    private static final int MAX_MONEY = 100_000;

    private final int money;

    public Money(int money) {
        validateMoney(money);
        this.money = money;
    }

    private void validateMoney(int money) {
        if (money < MONEY_PER_LOTTO || money % MONEY_PER_LOTTO != 0) {
            throw new IllegalArgumentException("로또는 천원 단위로만 구입할 수 있습니다.");
        }

        if (money > MAX_MONEY) {
            throw new IllegalArgumentException("로또는 한번에 최대 10만원까지만 구입할 수 있습니다.");
        }
    }

    public int getNumberOfLotto() {
        return money / MONEY_PER_LOTTO;
    }
}
