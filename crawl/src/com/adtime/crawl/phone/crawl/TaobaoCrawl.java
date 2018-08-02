package com.adtime.crawl.phone.crawl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.adtime.crawl.phone.utils.DataUpload;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import jp.jun_nama.test.utf7ime.helper.Utf7ImeHelper;

public class TaobaoCrawl extends UiAutomatorTestCase {

	
	
	public void testAlimama() throws IOException {
		int items = 0; 
		while(true){
			try {
				JSONObject json =  DataUpload.loadData();
				int status = json.getInt("status");
				if(status != 0 ){
					long start = System.currentTimeMillis();
					String code = json.getString("tklStr");
					Map<String, String> crawl = crawl(code);
					crawl.put("sign", json.getString("sign"));
					crawl.put("tklStr",code);
					DataUpload.uploadData(new JSONObject(crawl));
					System.out.println("爬取一条数据:"+crawl+"\t总耗时："+(System.currentTimeMillis()-start)+"ms");
					items ++;
					if(items >= 50) {
						getUiDevice().pressRecentApps();
						UiObject recentapp = new UiObject(new UiSelector().resourceId("com.android.systemui:id/task_view_thumbnail"));
						do{
							recentapp.waitForExists(2000);            
				            if(recentapp.exists()){
				                recentapp.swipeLeft(50);
				            }                
				        }while(recentapp.exists());   
						items = 0 ;
					}
				}
				Thread.sleep(500L);
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();//异常自行消化 
			}
		}
	}

	@SuppressWarnings("deprecation")
	private Map<String, String> crawl(String code) throws Exception{
		int i = 50;
		UiObject uio = new UiObject(new UiSelector().resourceId("com.example.mytest:id/copyValue"));
		if(uio.exists()){
			uio.setText(Utf7ImeHelper.e(code));
			uio = new UiObject(new UiSelector().resourceId("com.example.mytest:id/copy"));
			uio.click();
		}else{
			Runtime.getRuntime().exec("monkey -p com.example.mytest -v 1"); // 打开中间程序
			do {
				Thread.sleep(500L);
				uio = new UiObject(new UiSelector().resourceId("com.example.mytest:id/copyValue"));
				if (uio.exists()) {
					uio.setText(Utf7ImeHelper.e(code));
					uio = new UiObject(new UiSelector().resourceId("com.example.mytest:id/copy"));
					uio.click();
					i = 0;
				}
				i--;
			} while (!uio.exists() && i > 0);
		}
		
		Runtime.getRuntime().exec("monkey -p com.alimama.moon -v 1"); // 打开阿里妈妈
		Map<String, String> data = new HashMap<String, String>();
		boolean ishave = false;
		i = 50;
		do {
			Thread.sleep(100L);
			uio = new UiObject(new UiSelector().resourceId("com.alimama.moon:id/btn_check_detail"));
			if (uio.exists()) {// 说明存在
				uio.clickAndWaitForNewWindow();
				ishave = true;
				i=0;
			}
			if (!ishave) {
				final UiObject uio2 = new UiObject(new UiSelector().resourceId("com.alimama.moon:id/btn_i_got_it"));
				if (uio2.exists()) {// 说明不存在
					new Thread(){
						public void run(){
							try{
								uio2.click();
								Runtime.getRuntime().exec("monkey -p org.lxzh.midd -v 1"); // 打开中间程序
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}.start();
					
					return data;
				}
			}
			i--;
		} while ( i > 0 );
		
		if (ishave) {
			i = 100;
			do {
				Thread.sleep(100L);
				uio = new UiObject(new UiSelector().resourceId("mx_8"));
				if (uio.exists()) {// 说明存在
					UiObject  commission = new UiObject(new UiSelector().textMatches(".*预计.*"));
					data.put("commission", commission.getText());
					UiObject  sellNum = new UiObject(new UiSelector().textStartsWith("已售"));
					data.put("sellNum", sellNum.getText());
					new UiObject(new UiSelector().resourceId("mx_10")).clickAndWaitForNewWindow();
					i=0;
				}
				i--;
			} while (!uio.exists() && i > 0);
			
			i = 80;
			do {
				Thread.sleep(100L);
				uio = new UiObject(new UiSelector().descriptionMatches("仅复制分享文案"));
				if (uio.exists()) {// 说明存在
					uio.click();
					i=0;
				}
				i--;
			} while (!uio.exists() && i > 0);
			
			Runtime.getRuntime().exec("monkey -p com.example.mytest -v 1"); // 打开中间程序
			i = 50;
			do {
				Thread.sleep(1000L);
				uio = new UiObject(new UiSelector().resourceId("com.example.mytest:id/paste"));
				if (uio.exists()) {
					uio.click();
					data.put("data", new UiObject(new UiSelector().resourceId("com.example.mytest:id/pasteValue")).getText());
					i=0;
				}
				i--;
			} while (!uio.exists() && i > 0);
		}
		return data;
	}

}
