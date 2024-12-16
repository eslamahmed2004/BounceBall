import java.io.File;

public class Main {
    public static void main(String[] args) {

        File folder = new File("D:\\Alphabet") ;
        File[] files = folder.listFiles();
        for (File file: files) {
            System.out.println(file.getName());
        }
        int cnt = 0;
//        for (int i = 0; i < 10; i++) {
//            if(files[i].renameTo(new File("D:\\Alphabet\\" + (72 + i) +"-"+".png" ))) cnt++;
//        }
//
//        for (int i = 10; i < files.length; i++) {
//            if(files[i].renameTo(new File("D:\\Alphabet\\" + (72 + i) + "-" + files[i].getName()))) cnt++;
//        }
//

        for (int i = 0; i < files.length; i++) {
            if(files[i].renameTo(new File("D:\\BounceBall\\src\\PNG\\" + 0 + files[i].getName()))) cnt++;
        }
        System.out.println(cnt);

    }
}