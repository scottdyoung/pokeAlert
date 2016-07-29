package models;

import com.google.common.collect.Lists;
import java.util.List;

public final class PokeResponse {
    private String status;
    private String jobStatus;
    private List<Pokemon> pokemon = Lists.newArrayList();

    public PokeResponse(final String status,
                        final String jobStatus,
                        final List<Pokemon> pokemon) {
        this.status = status;
        this.jobStatus = jobStatus;
        if (null != pokemon) {
            this.pokemon.addAll(pokemon);
        }
    }

    public final String getStatus() {
        return this.status;
    }

    public final String getJobStatus() {
        return this.jobStatus;
    }

    public final List<Pokemon> getPokemon() {
        return Lists.newArrayList(this.pokemon);
    }
}
