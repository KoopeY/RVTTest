package model;

import enums.AccountOperationEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.exception.AccountOperationException;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "account")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "account")
    private String account;

    @Column(name = "balance")
    private Double balance;

    public void deposit(Double sum) {
        this.balance += sum;
    }

    public void withdraw(Double sum) throws AccountOperationException {
        if (sum > this.balance) {
            throw new AccountOperationException(
                    this.account,
                    AccountOperationEnum.WITHDRAW,
                    String.format("Not enough balance: Balance = %f, Need = %f", this.balance, sum)
            );
        }
        this.balance -= sum;
    }
}
