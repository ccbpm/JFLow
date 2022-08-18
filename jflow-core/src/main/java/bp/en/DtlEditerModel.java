package bp.en;

import bp.wf.BlockModel;

public enum DtlEditerModel {
    /// <summary>
    /// 从表的编辑模式
    /// </summary>

        /// <summary>
        /// 批量编辑
        /// </summary>
        DtlBatch(0),
        /// <summary>
        /// 查询编辑模式
        /// </summary>
        DtlSearch (1);

    public static final int SIZE = java.lang.Integer.SIZE;

    private int intValue;
    private static java.util.HashMap<Integer, DtlEditerModel> mappings;
    private static java.util.HashMap<Integer, DtlEditerModel> getMappings()  {
        if (mappings == null)
        {
            synchronized (BlockModel.class)
            {
                if (mappings == null)
                {
                    mappings = new java.util.HashMap<Integer, DtlEditerModel>();
                }
            }
        }
        return mappings;
    }

    private DtlEditerModel(int value)
    {intValue = value;
        getMappings().put(value, this);
    }

    public int getValue()  {
        return intValue;
    }

    public static DtlEditerModel forValue(int value)
    {return getMappings().get(value);
    }
}
