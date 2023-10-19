package com.example.weekdays.dto;

import com.example.weekdays.component.UserAccount;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;


@Data
@NoArgsConstructor //default 생성자 만들어주기
public class ProfileDto {

    @Length(max = 35)
    private String bio; //자기소개

    @Length(max = 50)
    private String url; //웹사이트 url

    @Length(max = 50)
    private String occupation; //직업

    @Length(max = 50)
    private String location; //지역

    private String profileImage;


    public ProfileDto(UserAccount userAccount) {
        this.bio = userAccount.getAccount().getBio();
        this.url = userAccount.getAccount().getUrl();
        this.occupation = userAccount.getAccount().getOccupation();
        this.location = userAccount.getAccount().getLocation();
        this.profileImage = userAccount.getAccount().getProfileImage();
    }



}