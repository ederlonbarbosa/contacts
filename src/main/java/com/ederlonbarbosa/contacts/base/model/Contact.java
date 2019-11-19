package com.ederlonbarbosa.contacts.base.model;

/**
 * @author Ederlon Barbosa
 */
public class Contact {

    private String name;
    private String cellphone;

    public Contact(final String name, final String cellphone) {
        this.name = name;
        this.cellphone = cellphone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", cellphone='" + cellphone + '\'' +
                '}';
    }

}