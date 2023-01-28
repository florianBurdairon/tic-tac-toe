package tictactoe;

/**
 * Enumerator of all protocol actions.
 * @author Bernard Alban
 * @author Burdairon Florian
 * @version 1
 */
public enum ProtocolAction {
    /**
     * Protocol actions send by the server
     */
    SelectDimensions,
    StartGame,
    EndGame,
    Play, // Position played by the opponent
    Validate,
    Error,
    NetworkError,
    ResumeGame,
    AskConfirmation,

    /**
     * Protocol actions send by the client
     */
    AnswerDimensions, // GridLength , GridDimension
    Place,
    WaitMessage,
    WaitPlayer,
    AddAI,
    Quit,
    SaveAndQuit,
    Confirmation,

    NONE;

    /**
     * Transform the protocol action into its index.
     * @return the index of the protocol action
     */
    public int getValue(){
        switch (this){
            case SelectDimensions: return 0;
            case StartGame: return 1;
            case EndGame: return 2;
            case Play: return 3;
            case Validate: return 4;
            case Error: return 5;
            case NetworkError: return 6;
            case ResumeGame: return 7;
            case AskConfirmation: return 8;
            case AnswerDimensions: return 9;
            case Place: return 10;
            case WaitMessage: return 11;
            case WaitPlayer: return 12;
            case AddAI: return 13;
            case Quit: return 14;
            case SaveAndQuit: return 15;
            case Confirmation: return 16;
            case NONE:
            default: return -1;
        }
    }

    /**
     * Transform an Integer into a protocol action.
     * @param id The index of the protocol we are looking
     * @return The protocol action corresponding
     */
    public static ProtocolAction fromInt(int id) {
        switch (id){
            case 0: return ProtocolAction.SelectDimensions;
            case 1: return ProtocolAction.StartGame;
            case 2: return ProtocolAction.EndGame;
            case 3: return ProtocolAction.Play;
            case 4: return ProtocolAction.Validate;
            case 5: return ProtocolAction.Error;
            case 6: return ProtocolAction.NetworkError;
            case 7: return ProtocolAction.ResumeGame;
            case 8: return ProtocolAction.AskConfirmation;
            case 9: return ProtocolAction.AnswerDimensions;
            case 10: return ProtocolAction.Place;
            case 11: return ProtocolAction.WaitMessage;
            case 12: return ProtocolAction.WaitPlayer;
            case 13: return ProtocolAction.AddAI;
            case 14: return ProtocolAction.Quit;
            case 15: return ProtocolAction.SaveAndQuit;
            case 16: return ProtocolAction.Confirmation;

            default: return ProtocolAction.NONE;
        }
    }
}
