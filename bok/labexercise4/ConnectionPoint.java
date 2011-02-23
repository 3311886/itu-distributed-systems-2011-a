package bok.labexercise4;

import java.io.Serializable;
import java.net.InetSocketAddress;

/***
 * Maybe remove this class, as it only wraps an inetsocketaddress at the moment 
 * @author Andreas
 *
 */
public class ConnectionPoint implements Serializable{
		private InetSocketAddress isa;
		
		public ConnectionPoint(InetSocketAddress isa) {
			this.isa = isa;
		}
		
		public InetSocketAddress getISA() {
			return this.isa;
		}
	
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			String str = String.format("HostName: %s\n", getISA().getHostName());
			sb.append(str);
			return sb.toString();
		}
}