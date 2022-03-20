package Server;

public class DBAServise implements AuthService {

    public String getNicknameByLoginAndPassword(String login, String password) {
        return DBHandler.getNicknameByLoginAndPassword(login, password);
    }

    public boolean registration(String login, String password, String nickname) {
        return DBHandler.registration(login, password, nickname);
    }

    public boolean changeNick(String oldNickname, String newNickname) {
        return DBHandler.changeNick(oldNickname, newNickname);
    }
}

