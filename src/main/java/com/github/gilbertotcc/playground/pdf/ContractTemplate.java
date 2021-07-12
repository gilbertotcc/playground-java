package com.github.gilbertotcc.playground.pdf;

import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.*;

import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
public class ContractTemplate implements AutoCloseable {

  PDDocument template;

  public static ContractTemplate loadFrom(InputStream pdfTemplate) throws IOException {
    PDDocument template = PDDocument.load(pdfTemplate);
    return new ContractTemplate(template);
  }

  public Contract fillWith(ContractData data) throws IOException {
    PDAcroForm form = template.getDocumentCatalog().getAcroForm();
    form.getFields().stream()
      .map(field -> String.format("Found field '%s', type %s", field.getFullyQualifiedName(), field.getFieldType()))
      .forEach(System.out::println);

    form.getField("Given Name Text Box").setValue(data.getGivenName());
    PDCheckBox esperantoCheck = (PDCheckBox) form.getField("Language 4 Check Box");
    if (data.isEsperantoEnabled()) {
      esperantoCheck.check();
    } else {
      esperantoCheck.unCheck();
    }

    PDChoice favouriteColorList = (PDChoice) form.getField("Favourite Colour List Box");
    favouriteColorList.setValue(data.getFavouriteColor());

    return new Contract(template, data);
  }

  @Override
  public void close() throws Exception {
    template.close();
  }
}
