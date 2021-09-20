package com.stonefacesoft.ottaa.utils.Accesibilidad;

public class ScreenScanningSettings {
    private boolean isSipAndPuff;
    private boolean isScroll;
    private boolean isScrollWithClick;
    private boolean isScreenScanning;
    private boolean isAvanzarYAceptar;
    private boolean isEnabled;
    private boolean isBarrido;

    public void setScreenScanning(boolean screenScanning) {
        isScreenScanning = screenScanning;
    }

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    public void setScrollWithClick(boolean scrollWithClick) {
        isScrollWithClick = scrollWithClick;
    }

    public void setSipAndPuff(boolean sipAndPuff) {
        isSipAndPuff = sipAndPuff;
    }

    public void setAvanzarYAceptar(boolean avanzarYAceptar) {
        isAvanzarYAceptar = avanzarYAceptar;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setBarrido(boolean barrido) {
        isBarrido = barrido;
    }

    public boolean isAvanzarYAceptar() {
        return isAvanzarYAceptar;
    }

    public boolean isScreenScanning() {
        return isScreenScanning;
    }

    public boolean isScroll() {
        return isScroll;
    }

    public boolean isScrollWithClick() {
        return isScrollWithClick;
    }

    public boolean isSipAndPuff() {
        return isSipAndPuff;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isBarrido() {
        return isBarrido;
    }
}
