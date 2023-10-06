package com.root32.dto;

import java.util.List;

public class PdfAttachment {
    private String name;
    private byte[] data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void add(List<PdfAttachment> generateProductBarCode) {
    }
}
