package test;

import com.company.QueenClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by andrkies1 on 12/10/2016.
 */
public class ChessAlgorithmTest {

    QueenClass queenObject=new QueenClass();

    @Test
    public void testInvalid(){
        assertEquals(queenObject.solve(0,3,3),-1);
        assertEquals(queenObject.solve(-1,3,3),-1);
        assertEquals(queenObject.solve(1,-1,3),-1);
    }

    @Test
    public void tooSmall(){
        assertEquals(queenObject.solve(3,2,3),0);
    }

    @Test
    public void test1(){
        assertEquals(queenObject.solve(1,3,3),9);
        assertEquals(queenObject.solve(1,2,3),6);
        assertEquals(queenObject.solve(1,0,3),0);
    }

    @Test
    public void testCreateRowExceptionSize1(){
        int[] result=queenObject.createRowExceptions(2, 3);

        assertEquals(1,result[0]);

        result=queenObject.createRowExceptions(3, 5);

        assertEquals(1,result[0]);
        assertEquals(2,result[1]);
    }


    @Test
    public void test2(){
        assertEquals(queenObject.solve(2,2,2),0);
        assertEquals(queenObject.solve(2,2,3),2);
        assertEquals(queenObject.solve(2,3,3),8);
        assertEquals(queenObject.solve(2,4,3),22);
        assertEquals(queenObject.solve(2,4,4),54);
        assertEquals(queenObject.solve(2,5,3),50);
    }

    @Test
    public void test3(){
        assertEquals(queenObject.solve(3,3,3),0);
        assertEquals(queenObject.solve(3,4,3),4);
        assertEquals(queenObject.solve(3,4,4),24);
    }

    @Test
    public void test4(){
        assertEquals(queenObject.solve(4,4,4),2);
        assertEquals(queenObject.solve(4,4,5),12);
    }

    @Test
    public void test5(){
        assertEquals(queenObject.solve(5,5,5),10);
        assertEquals(queenObject.solve(5,5,7),184);
    }

    @Test
    public void testFindNumberRepetitions(){
        int numExceptions=4, numSpaces=4;

        assertEquals(queenObject.findNumberRepetitions(numExceptions,numSpaces), 70);
    }






}
