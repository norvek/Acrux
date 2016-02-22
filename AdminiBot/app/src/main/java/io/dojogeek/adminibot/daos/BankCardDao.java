package io.dojogeek.adminibot.daos;

import java.util.List;

import io.dojogeek.adminibot.enums.CardTypeEnum;
import io.dojogeek.adminibot.models.BankCardModel;

public interface BankCardDao extends ConnectionDao {

    long createBankCard(BankCardModel bankCardModel);

    BankCardModel getBankCardById(long cardBankId);

    List<BankCardModel> getBankCards();

    long updateBankCard(BankCardModel bankCardModel, String where);

    long deleteBankCard(long bankCardId);

    List<BankCardModel> getBankCardByCartType(CardTypeEnum cardType);
}
