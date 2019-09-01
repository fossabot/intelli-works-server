import info.skyblond.velvet.scarlatina.KotlinMain;
import info.skyblond.velvet.scarlatina.models.message.Teacher;
import info.skyblond.velvet.scarlatina.operator.DataBaseOP;

import java.util.Scanner;

//TODO admin side panel.
//TODO write test units.
//TODO rebuild codes with proper packages.

public class Main {
    public static void main(String[] args){
//        KotlinMain.INSTANCE.pinyin();
//        if(true)
//            return;
//
//        System.out.println(TokenOP.Companion.generateUid());
//        System.out.println(TokenOP.Companion.generateKey());
        if(args != null && args.length >= 1)
            operation();
        else
            KotlinMain.INSTANCE.main();
    }
    
    private static void operation() {
        //TODO insert new teacher
        generate();
    }
    
    private static void generate() {
        boolean flag;
        Scanner scanner = new Scanner(System.in);
        DataBaseOP.INSTANCE.fetchBuilding();
        while(true){
            System.out.println("教师列表:");
            flag = true;
            Teacher[] teachers = DataBaseOP.INSTANCE.fetchTeachers(false);
            for(int i = 0; i < teachers.length; i++){
                System.out.println("\t[" + i + "]: " + teachers[i].getName());
            }
            String line; int index = -1;
            do{
                System.out.println("请输入生成邀请码的教师编号（输入q退出）:");
                line = scanner.nextLine();
                if(line.trim().toLowerCase().equals("q"))
                    System.exit(0);
                try{
                    index = Integer.parseInt(line);
                    if(index >= 0 && index <= teachers.length)
                        flag = false;
                }catch (Exception e){ }
            }while(flag);
            
            String token = KotlinMain.INSTANCE.generateCode(teachers[index].getUid());
            System.out.println(teachers[index].getName());
            System.out.println(token);
            System.out.println();
            //  m4sm3WaB31Jk84kXxlRyN5TYA7u39WuvIRyXPv_xljmb
    
        }
    }
}
