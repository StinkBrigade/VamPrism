package kimera.really.works.vamprism.common.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public final class CommonUtil
{
    public static List<BlockPos> readBlockPosListNBT(CompoundNBT parentNBTTagCompound, String key)
    {
        List<BlockPos> returnList = new ArrayList<>();

        INBT returnListINBT = parentNBTTagCompound.get(key);
        if(returnListINBT instanceof ListNBT)
        {
            ListNBT returnListNBT = (ListNBT) returnListINBT;

            for(int i = 0; i < returnListNBT.size(); i++)
            {
                INBT listEntryINBT = returnListNBT.get(i);
                if(listEntryINBT instanceof CompoundNBT)
                {
                    returnList.add(NBTUtil.readBlockPos((CompoundNBT) listEntryINBT));
                }
            }
        }

        return returnList;
    }

    public static void writeBlockPosListNBT(CompoundNBT parentNBTTagCompound, String key, List<BlockPos> list)
    {
        ListNBT listNBT = new ListNBT();
        for(int i = 0; i < list.size(); i++)
        {
            listNBT.add(NBTUtil.writeBlockPos(list.get(i)));
        }
        parentNBTTagCompound.put(key, listNBT);
    }
}
