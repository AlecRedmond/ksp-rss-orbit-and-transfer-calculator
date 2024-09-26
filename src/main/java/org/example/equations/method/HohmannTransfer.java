package org.example.equations.method;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.equations.application.Keplerian;

@Data
@Getter
@Setter
public class HohmannTransfer {
    private Keplerian keplerianOne;
    private Keplerian keplerianTwo;



}
