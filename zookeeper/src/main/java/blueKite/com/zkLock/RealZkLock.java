package blueKite.com.zkLock;

import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkDataListener;

public class RealZkLock extends AbsLock{

	@Override
	public boolean getLock() {
		try {
			zkClient.createEphemeral(Path);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void awaitLock() {
		
		IZkDataListener listener = new IZkDataListener() {
			
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				if(latch!=null) {
					latch.countDown();
				}
			}
			
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
			}
		};
		
		zkClient.subscribeDataChanges(Path, listener);
		
		if(zkClient.exists(Path)) {
			latch = new CountDownLatch(1);
			try {
				latch.await();
				latch = null;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		zkClient.unsubscribeDataChanges(Path, listener);
	}

}
