package fr.cneftali.spring.test.feature.jms.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class Request implements Serializable {

    private static final long serialVersionUID = -7489791890082564339L;

    private Long id;
    private String body;

}
