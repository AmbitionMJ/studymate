package com.example.weekdays.dto;

import com.example.weekdays.domain.entity.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationsDto {

    private boolean keywordCreatedByEmail; //키워드 알림을 메일로 받는다.

    private boolean keywordCreatedByWeb; //키워드 알림을 웹으로 받는다.



    public NotificationsDto(Account account){

        this.keywordCreatedByEmail=account.isKeywordCreatedByEmail();
        this.keywordCreatedByWeb=account.isKeywordCreatedByWeb();

    }

}
