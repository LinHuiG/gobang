
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class jpy {




    public static int[] getAns(int[][] m) throws IOException {
        FileOutputStream fos = new FileOutputStream("temp.txt");
        for(int i=0;i<19;i++)
        {
            for(int j=0;j<19;j++)
            {
                fos.write((m[i][j]+"").getBytes());
            }
        }
        fos.close();

        Runtime.getRuntime().exec("python D:\\专业\\作业\\算法课设\\gobang\\src\\AI.py");
        //调用AI.PY 这句要改

        FileInputStream fin=new FileInputStream("temp.txt");
        byte[] b=new byte[64];
        fin.read(b);
        String s=new String(b);
        System.out.println(s);
        fin.close();
        int x=Integer.valueOf(s.split(" ")[0].trim());
        int y=Integer.valueOf(s.split(" ")[1].trim());
        int[] ans=new int[2];
        ans[0]=x;
        ans[1]=y;
        return ans;
    }



    public static void main(String []args) throws IOException {
        int[][] an=new int[19][19];
        for(int i=0;i<19;i++)
        {
            for(int j=0;j<19;j++)
            {
                an[i][j]=0;
            }
        }
        int[] ans=getAns(an);
        System.out.println(ans[0]+"   "+ans[1]);
    }
}
