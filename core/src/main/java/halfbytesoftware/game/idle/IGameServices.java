package halfbytesoftware.game.idle;

public interface IGameServices {
    IPlatformServices getPlatformServices();
    void setNextScreen(AbstractScreen screen);
    GameState getGameState();
}
