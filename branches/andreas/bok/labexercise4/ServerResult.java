package bok.labexercise4;

/***
 * Enum of server responses 
 * 
 * @author Andreas
 *
 */
public enum ServerResult {	
	Added, AddedAndBroadCast, Removed,RemovedAndBroadCast, AlreadyAdded, UnknownError, 
	RemoveServerInitiatedFromServer, RemoveServerInitiatedFromJoiner, 
	JoiningServerRemoved
	
	
}