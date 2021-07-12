package com.github.gilbertotcc.playground.pdf;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.io.OutputStream;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Contract implements AutoCloseable {

  private final PDDocument document;

  @Getter
  private final ContractData data;

  public void save(OutputStream outputStream) throws IOException {
    document.save(outputStream);
  }

  @Override
  public void close() throws Exception {
    document.close();
  }
}
