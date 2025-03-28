package ro.mpp2024.domain;

public class Game extends Entity<Long>{
    private String teamA;
    private String teamB;
    private String date;
    private float price;
    private Type type;
    private int seats;

    public Game(String teamA, String teamB, String date, float price, Type type, int seats) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.date = date;
        this.price = price;
        this.type = type;
        this.seats = seats;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}
