package com.company;

/**
 * Created by andrkies1 on 12/10/2016.
 */
public class QueenClass {
    public int solve(int numQueens, int rowsCount, int colsCount) {

        //This is the minimum values between the row and column count
        int minRowsCols=Math.min(rowsCount,colsCount);

        //If rows or cols has a negative count or numQueens is 0 or less, return -1 signalling invalid input
        if(minRowsCols<0||numQueens<1){
            return -1;
        }

        //If number of queens is 1, return the product of the row and col count
        if (numQueens==1)
            return rowsCount*colsCount;

        //If the minimum value for the dimensions is less than the number of queens, it is not possible for the queens
        //to fit
        if(numQueens>minRowsCols)
            return 0;

        //If the number of queens is greater than 1, use solveHelper
        return solveHelper(numQueens,rowsCount,colsCount);
    }

    private int solveHelper(int numQueens, int rowsCount, int colsCount) {

        //This is the minimum value between the dimensions given
        int minRowsCols=Math.min(rowsCount,colsCount);

        //The row count is given the max value between the dimensions and the column is assigned to the minimum value
        //in order to create uniformity and for recursive purposes
        rowsCount=Math.max(rowsCount,colsCount);
        colsCount=minRowsCols;

        //This keeps track of the possibility number and is eventually returned
        int possibilities=0;

        //Return the result when the max dimension value has 1 subtracted from it. Take this result and double it
        //since the pattern that qualify from the smaller dimensions can be fitted twice on a slightly larger area.
        if(rowsCount-1>=numQueens){
            possibilities=solveHelper(numQueens,rowsCount-1,colsCount)*2;
        }


        //This is the middle index of the rows. This is used to check the possibilities when the first index is on the
        //the left side of the board and also used to check the center when there is an odd number of rows since
        //the valid patterns found here have an equivalent value when it is flipped from left to right. Therefore,
        //there are always an even number of possibilities.
        int middle=colsCount/2;

        //In cases where there are more rows than queens, this keeps track of what rows will be excluded for this
        //loop iteration
        int[] rowExceptions= createRowExceptions(numQueens, rowsCount);

        //Pass in the number of exceptions and number of spaces to find out how many different possibilities that there
        //are for the row exceptions. This is used so that we can infer what the pattern matches will be if we flip
        //the grid upside down. Also, if the number of repetitions are found ahead of time, less care needs to be
        //taken to not cause an error while incrementing the row exceptions.
        int numRepetitions=findNumberRepetitions(rowsCount-numQueens,numQueens-2);

        //The repetition middle is found so that the first half of the row exceptions can be evaluated. We don't need
        //to find the valid patterns after the first half of the results since the same matching patterns for the first
        //half are applicable if the grid is flipped upside down.
        int repetitionMiddle=numRepetitions/2;

        for(int repetition=0;repetition<repetitionMiddle;++repetition){
            //A new int array is created each time since different rows are excluded each time
            int[] rowValues=new int[rowsCount];

            //This changes the int array to put a -1 in any spots that correspond to an exempted row.
            rowValues=applyExceptions(rowValues,rowExceptions);

            //The possibility count for the first half of the exceptions are doubled since the pattern can be flipped
            //upside down
            possibilities+=checkPossibilities(middle, rowValues, colsCount, numQueens)*2;

            //The row exceptions are incremented, for example: 1,2,3,4 -> 1,2,3,5
            rowExceptions=incrementRowExceptions(rowsCount,rowExceptions);
        }

        //If there is a middle value, is evaluated here
        if(numRepetitions%2==1){
            //A new int array is created each time since different rows are excluded each time
            int[] rowValues=new int[rowsCount];

            //This changes the int array to put a -1 in any spots that correspond to an exempted row.
            rowValues=applyExceptions(rowValues,rowExceptions);

            //The possibility count for the middle set of exceptions is not doubled since it can't be flipped upside
            //down
            possibilities+=checkPossibilities(middle, rowValues, colsCount, numQueens);
        }

        return possibilities;
    }

    //This method works as precursor to the other method by the same name to return the possibility count
    public int checkPossibilities(int middle, int[] rowValues, int colsCount, int numQueens){
        //This loop to goes through the first half of the loop for the first value
        int possibilities = 0;
        for (int i = 0; i < middle; ++i) {
            rowValues[0] = i;
            possibilities += checkPossibilities(rowValues, colsCount, 0);

        }

        //Do the checkPossibilities method for the middle value if there is one
        if (colsCount % 2 == 1) {

            rowValues[0] = middle;

            int secondQualifyingIndex=findNextQualifyingIndex(rowValues);

            //This checks the first half of the 2nd row when the first row is the center
            for (int i = 0; i < middle; ++i) {
                rowValues[secondQualifyingIndex] = i;

                //If these two values are valid with one another, we dive deeper with the rest of the rows
                if (validValue(rowValues, secondQualifyingIndex)) {
                    //If there are only two queens, we don't need to look further
                    if(numQueens==2){
                        possibilities+=2;
                    }
                    else {
                        possibilities += checkPossibilities(rowValues, colsCount, secondQualifyingIndex);
                    }
                }
            }
        }

        return possibilities;
    }

    //This returns the first index besides the first one that does not have -1 in it
    private int findNextQualifyingIndex(int[] rowValues) {
        for(int i=1;i<rowValues.length;++i){
            if(rowValues[i]!=-1){
                return i;
            }
        }
        return -1;
    }

    //For each row that has an exception, place a -1 in that spot
    private int[] applyExceptions(int[] rowValues, int[] rowExceptions) {

        //If there are no exceptions, just return the row values as is
        if(rowExceptions[0]==-1){
            return rowValues;
        }

        //If the first row value is not -1, place a -1 in each exempted row
        for (int rowException : rowExceptions) {
            rowValues[rowException] = -1;
        }

        return rowValues;
    }


    //This method creates the first set of row exceptions
    public int[] createRowExceptions(int numQueens, int rowsCount) {
        int length=rowsCount-numQueens;

        //If rows equals the number of queens, there aren't any exceptions
        if(length==0){
            return new int[]{-1};
        }

        //The length of the exception list is the number of rows minus the number of queens
        int[] result=new int[length];


        //The first spot is set to 1
        result[0]=1;

        //For the rest of the spots, put in incremental values based on the previous value plus one
        int currentIndex=1;

        while (currentIndex<length){
            result[currentIndex]=result[currentIndex-1]+1;
            ++currentIndex;
        }

        return result;
    }

    //The row exceptions are incremented, for example: 1,2,3,4 -> 1,2,3,5
    public int[] incrementRowExceptions(int rowCount, int[] rowExceptions) {

        //the last index that can be included in the excluded indexes is the index before the last one
        int exceptionLimit=rowCount-2;

        //This is the boundary of the current index
        int boundary=rowExceptions.length;

        //Start at the last index of the array
        int currentIndex=boundary-1;

        //While the current index is less than the boundary, increment numbers accordingly
        while (currentIndex<boundary){
            ++rowExceptions[currentIndex];

            //If the current value is greater than the limit, set its value to 0 and increment the previous value,
            //otherwise, move onto the next index
            if(rowExceptions[currentIndex]>exceptionLimit){
                rowExceptions[currentIndex]=0;
                --currentIndex;
            }
            else {
                ++currentIndex;
                if(currentIndex!=boundary){
                    rowExceptions[currentIndex]=rowExceptions[currentIndex-1];
                }
            }
        }

        return rowExceptions;
    }

    private int checkPossibilities(int[] rowValues, int limit, int indexBoundary) {
        int possibilities = 0;
        int currentIndex=indexBoundary+1;

        //Check for all possibilities while the current index is greater than the index boundary
        while(currentIndex>indexBoundary){

            //If the value doesn't put any of the other queens in danger, it is valid and we move onto the next
            //index
            if(validValue(rowValues,currentIndex)){
                ++currentIndex;

                //If the outside of the row values range is reached, the possibilities count is increased
                if (currentIndex==rowValues.length){

                    //Add two possibilities at a time to account for the mirror opposite arrangement
                    possibilities+=2;
                    --currentIndex;
                    ++rowValues[currentIndex];
                }

                //While the current index is exempted, move onto the next one
                while (rowValues[currentIndex]==-1){
                    ++currentIndex;
                }

            }

            //If the current value is not valid, increment the value
            else{
                ++rowValues[currentIndex];
            }

            //If the current value equals the limit, set it to zero and increment the previous value
            while(rowValues[currentIndex]==limit){
                rowValues[currentIndex]=0;
                --currentIndex;

                //Continue to move backwards until a non exempted index is found
                while (rowValues[currentIndex]==-1){
                    --currentIndex;
                }
                ++rowValues[currentIndex];
            }
        }

        return possibilities;
    }

    private boolean validValue(int[] rowValues, int startingIndex) {
        //Check to see if there is a value placed in the same column as a previous number, above the number diagonally
        // to the left, or above the number diagonally to the right

        if(rowValues[startingIndex]==-1){
            return true;
        }

        int currentIndex=startingIndex-1;
        int diagonalLeft=rowValues[startingIndex]-1;
        int diagonalRight=rowValues[startingIndex]+1;

        while (currentIndex>-1){
            if (rowValues[currentIndex] != -1) {
                if (rowValues[currentIndex]==rowValues[startingIndex]||
                    rowValues[currentIndex]==diagonalLeft||
                    rowValues[currentIndex]==diagonalRight){
                        return false;
                }
            }

            --diagonalLeft;
            ++diagonalRight;
            --currentIndex;
        }

        return true;
    }

    public int findNumberRepetitions(int numExceptions, int numSpaces){
        int minValue=Math.min(numExceptions,numSpaces);
        if(minValue==0)
            return 1;
        if(minValue==1)
            return numExceptions+numSpaces;

        int result=numSpaces+1;
        int newSpaceCount=1;

        while(numSpaces>0){
            result+=numSpaces*(findNumberRepetitions(numExceptions-2,newSpaceCount));

            ++newSpaceCount;
            --numSpaces;
        }


        return result;
    }
}
