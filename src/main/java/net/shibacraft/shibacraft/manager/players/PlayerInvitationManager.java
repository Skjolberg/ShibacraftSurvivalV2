package net.shibacraft.shibacraft.manager.players;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInvitationManager {

    //private final Set<UUID> pendingPlayers = new HashSet();
    private Map<UUID, UUID> requests = new HashMap();

    public void addPendingPlayer(UUID invitado, UUID presidente) {
        requests.put(invitado, presidente);
    }

    public void removePendingPlayer(UUID invitado) {

        requests.remove(invitado);
    }

    public boolean isPendingPlayer(UUID invitado) {

        return requests.containsKey(invitado);
    }

    public Map<UUID, UUID> getRequests() {
        return requests;
    }

    public UUID getPresidentUUID(UUID key) {
        return requests.get(key);
    }
}
