package tictactoe;

/**
 * Enumerator of all protocol actions.
 * @author Bernard Alban
 * @author Burdairon Florian
 * @version 1
 */
public enum ProtocolAction {
    SelectDimensions,
    StartGame,
    EndGame,
    Play, // Position played by the opponent
    Validate,
    /**
     * Errors list :
     *  - 0:Error on dimensions selection
     *  - 1:Error on play input -> Place already used
     *  - 2:Error on play input -> Place is not valid
     */
    Error,
    NetworkError,
    ResumeGame,
    AskConfirmation,
    OpponentDisconnected,
    AnswerDimensions, // GridLength , GridDimension
    Place,
    WaitMessage,
    Quit,
    Confirmation,

    NONE;

    /**
     * Transform the protocol action into its index.
     * @return the index of the protocol action
     */
    public int getValue(){
        return switch (this) {
            case SelectDimensions -> 0;
            case StartGame -> 1;
            case EndGame -> 2;
            case Play -> 3;
            case Validate -> 4;
            case Error -> 5;
            case NetworkError -> 6;
            case ResumeGame -> 7;
            case AskConfirmation -> 8;
            case OpponentDisconnected -> 9;
            case AnswerDimensions -> 10;
            case Place -> 11;
            case WaitMessage -> 12;
            case Quit -> 13;
            case Confirmation -> 14;
            default -> -1;
        };
    }

    /**
     * Transform an Integer into a protocol action.
     * @param id The index of the protocol we are looking
     * @return The protocol action corresponding
     */
    public static ProtocolAction fromInt(int id) {
        return switch (id) {
            case 0 -> ProtocolAction.SelectDimensions;
            case 1 -> ProtocolAction.StartGame;
            case 2 -> ProtocolAction.EndGame;
            case 3 -> ProtocolAction.Play;
            case 4 -> ProtocolAction.Validate;
            case 5 -> ProtocolAction.Error;
            case 6 -> ProtocolAction.NetworkError;
            case 7 -> ProtocolAction.ResumeGame;
            case 8 -> ProtocolAction.AskConfirmation;
            case 9 -> ProtocolAction.OpponentDisconnected;
            case 10 -> ProtocolAction.AnswerDimensions;
            case 11 -> ProtocolAction.Place;
            case 12 -> ProtocolAction.WaitMessage;
            case 13 -> ProtocolAction.Quit;
            case 14 -> ProtocolAction.Confirmation;
            default -> ProtocolAction.NONE;
        };
    }
}
