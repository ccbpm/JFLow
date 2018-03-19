package BP.WF.Entity;

public enum CCBPM_DType
	{
		/** 
		 siverlight 模式
		 
		*/
		CCFlow(0),
		/** 
		 V1.0
		 
		*/
		CCBPM(1),
		/** 
		 V2.0
		 
		*/
		BPMN(2);

		private int intValue;
		private static java.util.HashMap<Integer, CCBPM_DType> mappings;
		private synchronized static java.util.HashMap<Integer, CCBPM_DType> getMappings()
		{
			if (mappings == null)
			{
				mappings = new java.util.HashMap<Integer, CCBPM_DType>();
			}
			return mappings;
		}

		private CCBPM_DType(int value)
		{
			intValue = value;
			CCBPM_DType.getMappings().put(value, this);
		}

		public int getValue()
		{
			return intValue;
		}

		public static CCBPM_DType forValue(int value)
		{
			return getMappings().get(value);
		}
	}