package models;

public final class Pokemon {
    private long id;
    private String data;
    private long expiration_time;
    private int pokemonId;
    private float latitude;
    private float longitude;
    private String uid;
    private boolean is_alive;

    public Pokemon(
            final long id,
            final String data,
            final long expiration_time,
            final int pokemonId,
            final float latitude,
            final float longitude,
            final String uid,
            final boolean is_alive
    ) {
        this.id = id;
        this.data = data;
        this.expiration_time = expiration_time;
        this.pokemonId = pokemonId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.uid = uid;
        this.is_alive = is_alive;
    }

    public final long getId() {
        return this.id;
    }

    public final String getData() {
        return this.data;
    }

    public final long getExpiration_time() {
        return this.expiration_time;
    }

    public final int getPokemonId() {
        return this.pokemonId;
    }

    public final float getLatitude() {
        return this.latitude;
    }

    public final float getLongitude() {
        return this.longitude;
    }

    public java.lang.String getUid() {
        return this.uid;
    }

    public final boolean getIs_alive() {
        return this.is_alive;
    }
}
