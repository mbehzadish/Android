package com.example.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import android.app.Activity;
import android.os.Bundle;
import android.renderscript.RenderScript.Priority;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CalcActivity extends Activity {

    protected String func = "";
    protected String result = "0"; 
	
    public String input_err_fixing(String function)
    {
    	String main_func = "" ;
    	char[] chars = function.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if(chars[i] == '*' || chars[i] == '/' || chars[i] == '+' || chars[i] == '-')
			{
				if(i > 0){
					if(i != chars.length -1)
					{
						if (function.charAt(i-1)== '*' || function.charAt(i-1)== '/'){
							if(function.charAt(i)== '-' || function.charAt(i)== '+')
								main_func+= chars[i];
						}
						else if(function.charAt(i-1)== '+' || function.charAt(i-1)== '-'){
							main_func = main_func.substring(0,main_func.length()-1);
							if((function.charAt(i-1)== '+' && function.charAt(i)== '-')||
							   (function.charAt(i-1)== '-' && function.charAt(i)== '+'))
									main_func += "-" ;
							else if((function.charAt(i-1)== '+' && function.charAt(i)== '+')||
									(function.charAt(i-1)== '-' && function.charAt(i)== '-'))
									main_func+= "+" ;
							
						}
						else main_func += chars[i];
					}
				}
				else main_func += chars[i];
				chars[i] = ' ' ;
			}
			else main_func+= chars[i];
		}
		int tmp =-1;
		for(int i = main_func.length()-1;i >-1 ; i--)
		{
			if(main_func.charAt(i)>='0' && main_func.charAt(i)<='9')
			{
				tmp = i ;
				break;
			}
		}
		main_func = main_func.substring(0,tmp+1);
		return main_func ;
    }
    
    public 	ArrayList<Integer> get_operation_pos(String main_func)
    {
		ArrayList<Integer> operation_pos = new ArrayList<Integer>();
		
		for (int i = 0; i < main_func.length(); i++) {
			char tmp_char = main_func.charAt(i) ;
			if(tmp_char == '*' || tmp_char== '/' || tmp_char== '+' ||tmp_char == '-')
			{	
				if(operation_pos.size()>0)
				{
					if(i - operation_pos.get(operation_pos.size()-1) != 1)
						operation_pos.add(i) ;
				}
				else if(i != 0)
					operation_pos.add(i);
			}
		}
		return operation_pos;
    }
 
    public String funcCalc(String main_func){
    	ArrayList<Integer> operation_pos = get_operation_pos(main_func);
    	double res ;
    	if (operation_pos.size() < 1)
    	{
    		Log.e("tag",main_func);
    		return main_func ;
    	}
    	else
    	{
    		char tmp_char = main_func.charAt(operation_pos.get(0)) ;
    		if(tmp_char == '+' || tmp_char == '-'){
	    		String var1 = main_func.substring(0 , operation_pos.get(0));
	    		String var2 = main_func.substring(operation_pos.get(0));
	    		res = Double.parseDouble(funcCalc(var1)) + Double.parseDouble(funcCalc(var2));
	    		return Double.toString(res) ;
	    	}
	    	else{
	    		String var1 = main_func.substring(0 , operation_pos.get(0));
	    		String var2 ;
	    		String new_func ;
	    		if(operation_pos.size()>1)
	    			var2 = main_func.substring(operation_pos.get(0)+1,operation_pos.get(1));
	    		else var2 = main_func.substring(operation_pos.get(0)+1); 
	    		if (tmp_char == '*')
	    			res = Double.parseDouble(funcCalc(var1)) * Double.parseDouble(funcCalc(var2));
	    		else res = Double.parseDouble(funcCalc(var1)) / Double.parseDouble(funcCalc(var2));
	    		if(operation_pos.size()>1)
	    			new_func = Double.toString(res) + main_func.substring(operation_pos.get(1));
	    		else new_func = Double.toString(res) ;
	    		return funcCalc(new_func);
	    	}
    	}
    }
    
    public String setPercision(String str)  {
		String res ;
    	try{
			BigDecimal bd = new BigDecimal(str);
			res = bd.toString();
			DecimalFormat df = new DecimalFormat("#.#####");
			df.setRoundingMode(RoundingMode.HALF_UP);
			return df.format(bd);
		}catch(NumberFormatException  e){
			return  "";
		}
    }
    
    class Listener implements OnClickListener
    {
    	private String s ;
    	private TextView txt_show;
    	private TextView res_show;
    	
		public Listener(String s,TextView txt_show, TextView res_show){
			this.s = s ;
			this.txt_show=txt_show;
			this.res_show=res_show;
		}
    	@Override
		public void onClick(View v) {
			String res ;
    		func += s;
			txt_show.setText(func);
			String main_func = input_err_fixing(func);
			res = funcCalc(main_func);
			res = setPercision(res);
			if(res.length()>1){
				if(res.substring(res.length()-2) .equals(".0"))
					res = res.substring(0,res.length()-2);
			}
			res_show.setHint(res);
		}
    	
    }
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
	        setContentView(R.layout.calc);
	        
	        final TextView txt_show = (TextView) findViewById(R.id.txt_show);
	        final TextView res_show = (TextView) findViewById(R.id.res_show);
	        final Button zero = (Button) findViewById(R.id.zero);
	        zero.setOnClickListener(new Listener("0",txt_show,res_show));
	        final Button one = (Button) findViewById(R.id.one);
	        one.setOnClickListener(new Listener("1",txt_show,res_show));
	        final Button two = (Button) findViewById(R.id.two);
	        two.setOnClickListener(new Listener("2",txt_show,res_show));
	        final Button three = (Button) findViewById(R.id.three);
	        three.setOnClickListener(new Listener("3",txt_show,res_show));
	        final Button four = (Button) findViewById(R.id.four);
	        four.setOnClickListener(new Listener("4",txt_show,res_show));
	        final Button five = (Button) findViewById(R.id.five);
	        five.setOnClickListener(new Listener("5",txt_show,res_show));
	        final Button six = (Button) findViewById(R.id.six);
	        six.setOnClickListener(new Listener("6",txt_show,res_show));
	        final Button seven = (Button) findViewById(R.id.seven);
	        seven.setOnClickListener(new Listener("7",txt_show,res_show));
	        final Button eight = (Button) findViewById(R.id.eight);
	        eight.setOnClickListener(new Listener("8",txt_show,res_show));
	        final Button nine = (Button) findViewById(R.id.nine);
	        nine.setOnClickListener(new Listener("9",txt_show,res_show));
	        final Button add = (Button) findViewById(R.id.add);
	        add.setOnClickListener(new Listener("+",txt_show,res_show));
	        final Button sub = (Button) findViewById(R.id.sub);
	        sub.setOnClickListener(new Listener("-",txt_show,res_show));
	        final Button mul = (Button) findViewById(R.id.div);
	        mul.setOnClickListener(new Listener("*",txt_show,res_show));
	        final Button div = (Button) findViewById(R.id.mul);
	        div.setOnClickListener(new Listener("/",txt_show,res_show));
	        final Button dot = (Button) findViewById(R.id.flt);
	        dot.setOnClickListener(new Listener(".",txt_show,res_show));
	        final Button sign = (Button) findViewById(R.id.sign);
	        sign.setOnClickListener(new Listener("-", txt_show, res_show));
	        final Button equal = (Button) findViewById(R.id.equal);
	        equal.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					res_show.setHint("");
					String main_func = input_err_fixing(func);
					String res = funcCalc(main_func);
					res = setPercision(res);
					if(res.length()>1){
						if(res.substring(res.length()-2) .equals(".0"))
							res = res.substring(0,res.length()-2);
					}
					txt_show.setText(res);
					func = res ;
				}
			});
	        final Button del = (Button) findViewById(R.id.clr);
	        del.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if(func.length() >1)
						func = func.substring(0,func.length()-1);
					else func = "";
					txt_show.setText(func);
					String main_func = input_err_fixing(func);
					String res = funcCalc(main_func);
					res = setPercision(res);
					res_show.setHint(res);
				}
			});
	        del.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View arg0) {
					func = "" ;
					res_show.setHint("");
					txt_show.setText("");
					return true;
				}
			});
	        
		}catch(Exception e)
        {
        	Log.e("TAG",e.getMessage());
        } 
    }
}