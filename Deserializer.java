package src.deserializer;

import java.util.Stack;
/***
 * 此类的功能是把EDI发送的以竖线|为字段分割符的字符串转换称数组，每个数组元素按照顺序存储一个字段值。
 * 由于字符串中不是每一个竖线都是分隔符，有些竖线前面如果有一个？，则标明该竖线一个普通字段，因此解析方法要区分哪些竖线是分割符，哪些已经被？转义成普通字符
 * 
 * @author stevenmac
 *
 */
public class Deserializer {
	/**
	 * 该方法将出入的字符串转换成数组，每个数组按照顺序存储一个字段值
	 * @param str
	 * @return
	 */
	public String[] getArray(String str){
	      String[] rtn=null;
	      if(str==null)
	         return rtn;
	      Stack<StackItem> stack=new Stack<StackItem>();
	      char[] chars=str.toCharArray();
	      //循环处理每一个字符
	      for(char c:chars){
	    	  //弹出栈顶元素
	    	  StackItem stackTop=this.getStackTop(stack);
	         if(this.isPlainChar(c)){//如果当前字符是普通字符
	            if(stackTop.getItemType()=='|'){//如果栈顶元素是分隔符，则压入一个普通字符类型的栈元素，其值为当前字符
	                stack.push(new StackItem(String.valueOf(c),'c'));
	            }else if(stackTop.getItemType()=='?'){//如果栈顶元素类型为转义符,则压入一个普通字符类型的栈元素，其值为?加上当前字符
	                stack.push(new StackItem("?"+String.valueOf(c),'c'));
	            }else{//如果栈顶元素为普通元素,则压入一个普通字符类型的栈元素，其值为原栈顶元素值加上当前字符
	            	stackTop.appendItemValue(c);
	                stack.push(stackTop);
	            }
	         }else if(c=='|'){//如果当前字符是竖线
	            if(stackTop.getItemType()=='?'){//如果栈顶元素类型为转义符,则再次弹出栈顶元素x，将当前字符c追加至x值的尾部，然后再压x入栈
	                StackItem stackTop2=this.getStackTop(stack);
	                stackTop2.appendItemValue(c);
	                stack.push(stackTop2);
	            }else if(stackTop.getItemType()=='|'){//如果栈顶元素是分隔符，则先压入一个空值的普通栈，再压入一个分割符的栈
	                stack.push(new StackItem("",'c'));
	                stack.push(new StackItem("|",'|'));
	            }
	            else{///如果栈顶元素为普通元素,则弹出的栈顶元素重新入栈，再压入一个分隔符元素
	                stack.push(stackTop);
	                stack.push(new StackItem("|",'|'));
	            }
	         }else{//当前字符是?
	            if(stackTop.getItemType()=='?'){//如果栈顶元素是转义符，则再次弹出栈顶元素x，将？追加至x值的尾部，然后再压x入栈
	                StackItem stackTop2=this.getStackTop(stack);
	                stackTop2.appendItemValue('?');
	                stack.push(stackTop2);
	            }else if(stackTop.getItemType()=='|'){//如果栈顶元素是分隔符，则压入一个转义符元素
	                stack.push(new StackItem("?",'?'));
	            }else{///如果栈顶元素为普通元素,则弹出的栈顶元素重新入栈，再压入一个转义符元素
	                stack.push(stackTop);
	                stack.push(new StackItem("?",'?'));
	            }
	         }
	      }
	      //循环出栈，为返回值数组逐个赋值
	      rtn=new String[stack.size()];
	      for(int i=rtn.length-1;i>=0;i--){
	         rtn[i]=stack.pop().getItemValue();
	      }
	      
	      return rtn;
	   }
		/**
		 * 判断当前字符是不是普通字符
		 * @param c
		 * @return
		 */
	   private boolean isPlainChar(char c){
	      if(c=='?'||c=='|')
	         return   false;
	      else
	         return true;
	   }
	   /**
	    * 弹出栈顶元素，如果栈为空，则返回一个缺省的栈元素
	    * @param stack
	    * @return
	    */
	   private StackItem getStackTop(Stack<StackItem> stack){
	      if(stack.isEmpty())
	         return new StackItem();
	      else
	         return stack.pop();
	   }
	   /**
	    * StackItem是一个内部类，StackItem对象会被要入栈中
	    * @author stevenmac
	    *
	    */
	   class StackItem {
		   //栈元素的值
		   private String itemValue="";
		   //栈元素的类型,一共有三个类型:c代表普通字符，?代表转义符,|代表分隔符
		   private char	itemType='c';
		   public StackItem(){};
		   public StackItem(String itemValue,char itemType){
			   this.itemValue=itemValue;
			   this.itemType=itemType;
		   }
		   public String getItemValue() {
			   return itemValue;
		   }
		   public void setItemValue(String itemValue) {
			   this.itemValue = itemValue;
		   }
		   public char getItemType() {
			   return itemType;
		   }
		   public void setItemType(char itemType) {
			   this.itemType = itemType;
		   }
		   /**
		    * 在栈元素末尾追加一个字符
		    * @param c
		    */
		   public void appendItemValue(char c){
			   this.itemValue=this.itemValue+String.valueOf(c);
		   }
	   }
}
