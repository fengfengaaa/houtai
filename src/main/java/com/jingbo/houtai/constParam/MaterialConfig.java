package com.jingbo.houtai.constParam;

import com.jingbo.houtai.entity.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MaterialConfig {
    public final static List<Material> MaterialList = new ArrayList();//模板的优先级

    static{
        List<String> d1 = new ArrayList<>();
        d1.add(FootConst.PURPOSE);
        Material d1Material = new Material(MaterialType.D1.name(),d1,Arrays.asList(7));
        MaterialList.add(d1Material);

        List<String> e1 = new ArrayList<>();
        e1.add(FootConst.METATARSALPAIN);
        e1.add(FootConst.MORTONNEUROMA);
        Material e1Material = new Material(MaterialType.E1.name(),e1);
        MaterialList.add(e1Material);

        List<String> e4 = new ArrayList<>();
        e4.add(FootConst.PURPOSE);
        Material e4Material = new Material(MaterialType.E4.name(),e4, Arrays.asList(0,1,2,3));
        MaterialList.add(e4Material);

        List<String> e2 = new ArrayList<>();
        e2.add(FootConst.HEELPAIN);
        e2.add(FootConst.CALCANEALSPUR);
        e2.add(FootConst.ATHEELPAIN);
        e2.add(FootConst.TARSALARTHROPATHY);
        Material e2Material = new Material(MaterialType.E2.name(),e2);
        MaterialList.add(e2Material);

        List<String> e3 = new ArrayList<>();
        e3.add(FootConst.PLANTARFASCIITIS);
        Material e3Material = new Material(MaterialType.E3.name(),e3);
        MaterialList.add(e3Material);

        List<String> d2 = new ArrayList<>();
        d2.add(FootConst.SACCHAROSIS);
        d2.add(FootConst.RHEUMATISM);
        Material d2Material = new Material(MaterialType.D2.name(),d2);
        MaterialList.add(d2Material);

        List<String> e5 = new ArrayList<>();
        e5.add(FootConst.STIFFTOE);
        Material e5Material = new Material(MaterialType.E5.name(),e5);
        MaterialList.add(e5Material);

        List<String> d3 = new ArrayList<>();
        d3.add(FootConst.PURPOSE);
        Material d3Material = new Material(MaterialType.D3.name(),d3, Arrays.asList(0,1,2,3,5));
        MaterialList.add(d3Material);

        List<String> d4 = new ArrayList<>();
        d4.add(FootConst.PURPOSE);
        Material d4Material = new Material(MaterialType.D4.name(),d4, Arrays.asList(4));
        MaterialList.add(d4Material);

        List<String> e6 = new ArrayList<>();
        e6.add(FootConst.VARUSDEFORMITY);
        e6.add(FootConst.VALGUSDEFORMITY);
        Material e6Material = new Material(MaterialType.E6.name(),d4);
        MaterialList.add(e6Material);

        List<String> d5 = new ArrayList<>();
        d4.add(FootConst.PURPOSE);
        Material d5Material = new Material(MaterialType.D5.name(),d5, Arrays.asList(8));
        MaterialList.add(d5Material);
    }
}
