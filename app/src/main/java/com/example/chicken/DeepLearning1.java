package com.example.chicken;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;

public class DeepLearning1 {
    private static final String MODEL_NAME = "noembed20.tflite";
    private Interpreter.Options options = new Interpreter.Options();
    private Interpreter mInterpreter;
    private final Komoran komoran;
    private HashMap<String, float[]> embedmap;
    private ArrayList<String> MenuList;
    private ArrayList<String> LocList;
    private ArrayList<String> MoneyList;
    private ArrayList<String> MariList;
    public DeepLearning1(Activity activity, Komoran komo, HashMap<String, float[]> retmap) throws IOException {
        String[] menuarray = {"양념", "간장", "후라이","드", "프라이드", "즐겁"};
        String[] locarray = {"로", "으로", "에", "이요", "이여", "요", "인데요", "데요", "ㄴ데요", "ㄴ데"};
        String[] moneyarray = {"현금", "카드"};
        String[] mariarray = {"마리", "한", "반", "두", "세"};
        MenuList = new ArrayList<>(Arrays.asList(menuarray));
        LocList = new ArrayList<>(Arrays.asList(locarray));
        MoneyList = new ArrayList<>(Arrays.asList(moneyarray));
        MariList = new ArrayList<>(Arrays.asList(mariarray));
        mInterpreter = new Interpreter(loadModelFile(activity), options);
        komoran = komo;
        embedmap = retmap;
    }
    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {

        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_NAME);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public String[] getAnswer(String input) {
        String[] ans = {"asdf", "asdf", "asdf", "asdf"};
        float[][] outputLocations = new float [1][1];
        List<Token> tokens = komoran.analyze(input).getTokenList();
        String temp;
        String temp2;
        float[][][] embedlayer = new float[1][8][128];
        for(int i=0;i<8;i++){
            if(i<tokens.size()){
                temp = tokens.get(tokens.size()-i-1).getMorph();
                if(embedmap.containsKey(temp)){
                    embedlayer[0][i] = embedmap.get(temp);
                }
                else{
                    embedlayer[0][i] = embedmap.get("byunghyungod");
                }
            }
            else {
                embedlayer[0][i] = embedmap.get("byunghyungod");
            }
        }
        mInterpreter.run(embedlayer, outputLocations);
        float x = outputLocations[0][0];
        for(int i=0;i<tokens.size();i++){
            temp = tokens.get(i).getMorph();
            Log.d("aaaaaaaaaaa   ", temp);
            if(MenuList.contains(temp)){
                if(temp.equals("드")) temp = "후라이"+temp;
                if(temp.equals("프라이드")) temp = "후라이드";
                if(temp.equals("즐겁")) temp = "즐거운 매운맛";
                ans[0] = temp;
            }
            if(LocList.contains(temp)){
                if(i>0) {
                    temp2 = tokens.get(i - 1).getMorph();
                    if(!(temp2.equals("현금")||(temp2.equals("카드")))) {
                        int j = i-2;
                        while(j>=0){
                            if(tokens.get(j).getMorph().equals("마리")) break;
                            if(tokens.get(j).getMorph().equals("여기")) break;
                            if(tokens.get(j).getMorph().equals("저")) break;
                            if(tokens.get(j).getMorph().equals("저기")) break;
                            temp2 = tokens.get(j).getMorph() + temp2;
                            j--;
                        }
                        ans[1] = temp2;
                    }
                }
            }
            if(MoneyList.contains(temp)){
                ans[2] = temp;
            }
            if(MariList.contains(temp)){
                if(i>0) {
                    ans[3] = tokens.get(i - 1).getMorph();
                }
            }
        }
        return ans;
    }

}