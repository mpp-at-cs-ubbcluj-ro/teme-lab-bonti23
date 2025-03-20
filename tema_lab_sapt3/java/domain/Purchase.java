package ro.mpp2024.domain;

public class Purchase extends Entity<Long>{
    private String client;
    private Long game;
    private int seats;
    private String address;
    public Purchase(String client, Long game, int seats, String address) {
        this.client = client;
        this.game = game;
        this.seats = seats;
        this.address = address;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Long getGame() {
        return game;
    }

    public void setGame(Long game) {
        this.game = game;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
