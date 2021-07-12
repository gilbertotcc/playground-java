package com.github.gilbertotcc.playground.pdf;

import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class ContractTemplateTest {

  @Test
  void fillWithShouldCreateContract() throws IOException {
    var pdfTemplate = this.getClass().getClassLoader().getResourceAsStream("pdf/OoPdfFormExample.pdf");
    ContractTemplate contractTemplate = ContractTemplate.loadFrom(pdfTemplate);

    ContractData contractData = ContractData.builder()
      .givenName("Mario")
      .esperantoEnabled(true)
      .favouriteColor("Green")
      .build();

    Contract filledContract = contractTemplate.fillWith(contractData);

    FileOutputStream fileOutput = new FileOutputStream("POC-filledForm.pdf");
    filledContract.save(fileOutput);
    fileOutput.close();

    pdfTemplate.close();
  }
}
