package model;

public class CanhCao {
    private Integer id;
    private String tenCanhCao;
    private String moTa;

    public CanhCao() {
    }

    public CanhCao(Integer id, String tenCanhCao, String moTa) {
        this.id = id;
        this.tenCanhCao = tenCanhCao;
        this.moTa = moTa;
    }

    public CanhCao(String tenCanhCao, String moTa) {
        this.tenCanhCao = tenCanhCao;
        this.moTa = moTa;
        this.id = null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenCanhCao() {
        return tenCanhCao;
    }

    public void setTenCanhCao(String tenCanhCao) {
        this.tenCanhCao = tenCanhCao;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }


    public String toString() {
        return tenCanhCao;
    }
}