package com.ccaaii.base.utils;

import java.lang.reflect.Method;


/**
 * Local implementation to preserve limitations from API LEVEL 9
 *
 */
public class ReflectionHelper {
	
	/**
	 * Refer to Class.getDeclaredMethod, local implementation to preserve limitations from API LEVEL 9
	 * @param caller
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Method getDeclaredMethod(Class<?> caller, String methodName,
			Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
		Method result = null;
		if (caller != null) {
			Method[] methodList = caller.getDeclaredMethods();
			result = searchMethods(methodList, methodName, parameterTypes);			
		}
		if(result == null) {
			throw new NoSuchMethodException();
		}
		return result;
	}
	
	/**
	 * Refer to Class.getMethod, local implementation to preserve limitations from API LEVEL 9
	 * @param caller
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Method getMethod(Class<?> caller, String methodName,
			Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
		Method result = null;
		if( caller != null) {
			Method[] methodList = caller.getMethods();
			result = searchMethods(methodList, methodName, parameterTypes);
		}
		if(result == null) {
			throw new NoSuchMethodException();
		}
		return result;
	}
	
	private static Method searchMethods(Method[] methodList, String methodName, Class<?>[] parameterTypes){
		Method result = null;
		if(methodList != null) {
			for(int i=0;i<methodList.length;i++){
				if(methodName.equals(methodList[i].getName()) &&
				    checkParameters(parameterTypes, methodList[i].getParameterTypes()) &&
				    (result == null || result.getReturnType().isAssignableFrom(methodList[i].getReturnType()))){
					result = methodList[i];
				}
				
			}
		}
		return result;
	}
	
	private static boolean checkParameters(Class<?>[] param1, Class<?>[] param2) {
		if((param1 == null)){
			return ((param2 == null) || (param2.length == 0));
		}
		if((param2 == null)){
			return (param1.length == 0);
		}
		if(param1.length != param2.length){
			return false;
		}
		for(int i=0;i<param1.length;i++){
			if(!param1[i].equals(param2[i])){
				return false;
			}
		}				
		return true;
	}	


}
