package blueKite.com.zkLock;

import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.ZkClient;

public abstract class AbsLock implements Lock{
	
	private static String POST = "127.0.0.1:2181";
	
	protected static String Path = "/LOCK";
	
	protected ZkClient zkClient = new ZkClient(POST);
	
	protected CountDownLatch latch = null;
	

	@Override
	public void tryLock() {
		if(getLock()) {
			return;
		}else {
			awaitLock();
			tryLock();
		}
	}
	
	

	@Override
	//释放锁,删除临时节点
	public void unLock() {
		if(zkClient!=null) {
			zkClient.close();
		}
	}
	
	//获取锁
	public abstract boolean getLock();
	
	//等待获取锁
	public abstract void awaitLock();

}
