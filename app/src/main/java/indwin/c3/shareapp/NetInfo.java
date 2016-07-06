package indwin.c3.shareapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by sudhanshu on 23/6/16.
 */
public class NetInfo {

    private ConnectivityManager connManager = null;
        private WifiManager wifiManager = null;
        private WifiInfo wifiInfo = null;
    
                public NetInfo(Context context){
                connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                wifiInfo = wifiManager.getConnectionInfo();
        
                    }
    
                public int getNetworkType(){
                if(connManager == null)
                        return 0;
        
                        NetworkInfo netinfo = connManager.getActiveNetworkInfo();
                return netinfo.getType();
            }
    
                public String getWifiAddress(){
                if((wifiManager ==null)||(wifiInfo==null))
                        return "";
                int ipAddress = wifiInfo.getIpAddress();
                return String.format("%d.%d.%d.%d",(ipAddress & 0xff),(ipAddress>> 8 & 0xff),(ipAddress>> 16 & 0xff),(ipAddress>> 24 & 0xff));
        
                    }
    
                public String getWifiMacAddress(){
                if((wifiManager ==null)||(wifiInfo==null))
                        return "";
        
                        return wifiInfo.getMacAddress();
            }
    
                public String getWifiSSID(){
                if((wifiManager ==null)||(wifiInfo==null))
                        return "";
        
                        return wifiInfo.getSSID();
        
                    }
    
                public String getIPAddress(){
                String ipaddress = "";
                try{
                        Enumeration<NetworkInterface> enumnet = NetworkInterface.getNetworkInterfaces();
                        NetworkInterface netinterface = null;
                        while (enumnet.hasMoreElements()){
                                netinterface = enumnet.nextElement();
                                for(Enumeration<InetAddress> enumip = netinterface.getInetAddresses(); enumip.hasMoreElements();){
                                        InetAddress inetAddress = enumip.nextElement();
                                        if(!inetAddress.isLoopbackAddress()){
                                                ipaddress = inetAddress.getHostAddress();
                                                break;
                                            }
                                    }
                            }
                    } catch (SocketException e) {
                       e.printStackTrace();
                    }
                return ipaddress;
            }
    

}
