package com.github.gilbertotcc.playground.pdf;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ContractData {
  String givenName;
  boolean esperantoEnabled;
  String favouriteColor;
}
