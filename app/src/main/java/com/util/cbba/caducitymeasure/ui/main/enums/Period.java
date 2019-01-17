package com.util.cbba.caducitymeasure.ui.main.enums;

public enum Period {
    EXPIRE_TODAY("Vencen Hoy"),
    EXPIRE_SOON("Vencen Pronto"),
    EXPIRE_ALL("Mostrar Todo"),
    EXPIRE_ALL_BY_DATE("Mostrar Todo Por Fecha")
    ;

    private final String label;

    Period(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;            // What to display in the Spinner list.
    }
}
