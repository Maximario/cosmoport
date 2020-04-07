package com.space.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "ship")
public class Ship {
    private Long id;            // ship id
    private String name;        // ship name (up to 50)
    private String planet;      // planet of ship's stay (up to 50)
    private ShipType shipType;  // ship type
    private Date prodDate;      // ship production date [2800..3019]
    private Boolean isUsed;     // is ship used or new
    private Double speed;       // max speed of ship [0.01..0.99]
    private Integer crewSize;   // number of crew members [1..9999]
    private Double rating;      // ship rating

    public Ship(){}

    public Ship(String name, String planet, ShipType shipType, Date prodDate, Boolean isUsed, Double speed, Integer crewSize) {
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.isUsed = isUsed;
        this.speed = speed;
        this.crewSize = crewSize;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "planet")
    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    @Column(name = "shipType")
    @Enumerated(EnumType.STRING)
    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    @Column(name = "prodDate")
    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    @Column(name = "isUsed")
    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    @Column(name = "speed")
    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    @Column(name = "crewSize")
    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    @Column(name = "rating")
    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void updateRating() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getProdDate().getTime());
        Double rating = (double) ((int) Math.round(100 * 80 * getSpeed() * (getUsed() ? 0.5 : 1) / (double) (3019 - calendar.get(Calendar.YEAR) + 1))) / 100;
        setRating(rating);
    }

    @Override
    public String toString() {
        return "Ship{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", planet='" + planet + '\'' +
                ", shipType=" + shipType +
                ", prodDate=" + prodDate +
                ", isUsed=" + isUsed +
                ", speed=" + speed +
                ", crewSize=" + crewSize +
                ", rating=" + rating +
                '}';
    }

    public static void main(String[] args) {
        Calendar prodDateValue = Calendar.getInstance();
        prodDateValue.setTimeInMillis((new Date()).getTime());
        prodDateValue.add(Calendar.YEAR, 1000);
        ShipType shipTypeValue = ShipType.MERCHANT;
        Double speedValue = Double.parseDouble("0.81");
        Integer crewSizeValue = Integer.parseInt("5000");
        Boolean isUsedValue = false;
        Ship ship = new Ship("Devastator", "Venice", shipTypeValue, prodDateValue.getTime(), isUsedValue, speedValue, crewSizeValue);
        Double rating = (double) ((int) Math.round(100 * 80 * speedValue * (isUsedValue ? 0.5 : 1) / (double) (3019 - prodDateValue.get(Calendar.YEAR) + 1))) / 100;
        ship.setRating(rating);
        System.out.println(prodDateValue.get(Calendar.YEAR));
        System.out.println("isUsedValue ? 0.5 : 1 --- " + (isUsedValue ? 0.5 : 1));
        System.out.println("speedValue * (isUsedValue ? 0.5 : 1) --- " + (speedValue * (isUsedValue ? 0.5 : 1)));
        System.out.println((80 * speedValue * (isUsedValue ? 0.5 : 1) / (double) (3019 - prodDateValue.get(Calendar.YEAR) + 1)));
        System.out.println(ship.toString());
    }
}
