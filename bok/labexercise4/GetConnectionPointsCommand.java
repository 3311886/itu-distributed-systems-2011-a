package bok.labexercise4;

import java.io.IOException;
import java.net.InetSocketAddress;

public class GetConnectionPointsCommand extends ServerCommand {

	public GetConnectionPointsCommand(InetSocketAddress cp1, InetSocketAddress cp2) {
		super(cp1, cp2);
	}

	@Override
	public Object Execute(PhonebookServer server) throws IOException {
		return server.getConnectionPoints();
	}
}
