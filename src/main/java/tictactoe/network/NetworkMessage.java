package tictactoe.network;

/**
 * Network message class.
 * Stocks a protocol action and its parameters if any.
 * @author Bernard Alban
 * @version 1
 */
public class NetworkMessage {
    private ProtocolAction protocolAction;
    private String[] parameters;

    /**
     * Generate an empty network message.
     * CAUTION : Do not send this as is, you must use {@link NetworkMessage#setProtocolAction(ProtocolAction)}  setProtocolAction()} before.
     */
    public NetworkMessage() {}

    /**
     * Generate a network message with the given protocol action.
     * @param protocolAction The protocol action of the message
     */
    public NetworkMessage(ProtocolAction protocolAction) {
        this(protocolAction, null);
    }

    /**
     * Generate a network message with the given protocol action and the parameters.
     * @param protocolAction The protocol action of the message
     * @param parameters parameters of the message
     */
    public NetworkMessage(ProtocolAction protocolAction, String[] parameters){
        this.protocolAction = protocolAction;
        this.parameters = parameters;
    }

    /**
     * Return the protocol action of the message.
     * @return The protocol action of the message
     */
    public ProtocolAction getProtocolAction(){
        return this.protocolAction;
    }

    /**
     * Return the parameters of the message.
     * @return The parameters of the message
     */
    public String[] getParameters(){
        return this.parameters;
    }

    /**
     * Set the protocol action of the message to the one given.
     * @param protocolAction The new protocol action to use for this message
     */
    public void setProtocolAction(ProtocolAction protocolAction) {
        this.protocolAction = protocolAction;
    }

    /**
     * Set the parameters of the message to the ones given.
     * @param parameters The new parameters to use for this message
     */
    public void setParameters(String[] parameters){
        this.parameters = parameters;
    }
}
