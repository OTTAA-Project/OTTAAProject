package com.stonefacesoft.ottaa.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.stonefacesoft.ottaa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gonzalo on 2/26/18.
 */

public class DibujarGraficoEstadisticos {
    int cant=0;
    Context context;
    private final CombinedChart mChart;
    private final PieChart pieChart;
    protected String[] mMonths,mTurno,mDia ;
    private final DatosDeUso mDatosDeUso;
    private final CombinedChart bubbleChart;
    private final SharedPreferences mSharedPrefsDefault;
    /**Constructor
     * @param context contexto de donde viene para crear el mDatosDeUso
     * @param datosDeUso datos de donde voy a tomar la informacion
     */

    public DibujarGraficoEstadisticos(Context context, DatosDeUso datosDeUso) {

        this.pieChart =new PieChart(context);
        this.bubbleChart=new CombinedChart(context);
        this.mChart=new CombinedChart(context);
        this.mDatosDeUso = datosDeUso;
        this.context=context;
        mSharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(context);
        dibujarCombinedChart(bubbleChart,1);
        dibujarCombinedChart(mChart,0);//dibujo el combined chart
        dibujarPieChar();//dibujo el piechart



    }




    //metodos relacionados con el combined chart
    /**
     *  este metodo se encarga de dibujar los graficos estadisticos de manera combinada
     *@param mCombineChart parametro encargado de recibir el grafico a dibujar
    * @param posd metodo encargado de dibujar el tipo de grafico
    * */
    private void dibujarCombinedChart(CombinedChart mCombineChart,int posd)
    {

        setCombineChart(mCombineChart);

        CombinedData data = new CombinedData();

        switch (posd){
            case 0:
                data.setData(generateBarData());
                ejesYCombinedChart(mCombineChart,data,0f,false);
                // ejesXCombinedChart(mCombineChart,data,0f,0);
                //setLegend(mCombineChart);

                break;
            case 1:
                data.setData(generateBubbleData());
                // ejesXCombinedChart(mCombineChart,data,0f,1);
                ejesYCombinedChart(mCombineChart,data,0f,true);


                break;
        }


        mCombineChart.setData(data);

        mCombineChart.invalidate();


    }
    //Seteo el combined chart
    /**
     * este metodo se encarga de dibujar el fondo del del dibujo
     * @param mCombineChart grafico combinado a modificar
     * */
    private void setCombineChart(final CombinedChart mCombineChart)
    {
        mCombineChart.getDescription().setText("This is testing Description");
        mCombineChart.setBackgroundColor(Color.WHITE);
        mCombineChart.setDrawGridBackground(true);
        mCombineChart.setDrawBarShadow(true);
        mCombineChart.setHighlightFullBarEnabled(true);
        mCombineChart.setPinchZoom(true);

        // draw bars behind lines
        mCombineChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE,CombinedChart.DrawOrder.BUBBLE
        });

    }
    //etiquetas
    /**
     * este metodo se encarga de escribir las etiquetas de informacion
     * @param mCombineChart grafico combinado donde se van a poner las etiquetas
     * */
    public void setLegend(CombinedChart mCombineChart)
    {
        mCombineChart.getLegend().setEnabled(true);
        Legend l =mCombineChart.getLegend();




    }
    //dibujo el eje de las y
    /**
     * metodo encargado de dibujar el eje de las y
     * @param mCombineChart grafico combinado
     * @param data  datos con los que se va a llenar el grafico
     * @param mAxisminimum  eje de las abcisas minimo
     * @param isbubbleChart  pregunto si es grafico de burbuja o no
     * */
    public void ejesYCombinedChart(CombinedChart mCombineChart,CombinedData data,float mAxisminimum,boolean isbubbleChart)
    {

        mTurno=new String[4];
        mTurno[0]="";
        mTurno[1]=context.getString(R.string.dmanana);
        mTurno[2]=context.getString(R.string.dtarde);
        mTurno[3]=context.getString(R.string.dnoche);
        if(!isbubbleChart)
        {YAxis rightAxis = mCombineChart.getAxisRight();
            rightAxis.setDrawGridLines(false);
            rightAxis.setAxisMinimum(mAxisminimum); // this replaces setStartAtZero(true)
            rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
            rightAxis.setGranularity(1f);

            rightAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {

                    return mTurno[(int) value % mTurno.length];

                }
            });
        }


        YAxis leftAxis = mCombineChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(mAxisminimum); // this replaces setStartAtZero(true)\
        if(isbubbleChart) {
            leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mTurno[(int) value % mTurno.length];
            }
        });
        }






    }

    public void ejesXCombinedChart(CombinedChart mCombineChart, CombinedData data, float mAxisMinimun, int tipo)
    {

        XAxis xAxis = mCombineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(mAxisMinimun);
        xAxis.setGranularity(1f);
        int size = mDatosDeUso.getGruposOrdenados().size();
        if(size>6)
        { size=6;}

       // cargarGrupos(size);
        cargarDias();
        String[] mArray=null;
        switch (tipo)
        {
            case 0:
                mArray=mMonths;
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return mMonths[(int) value % mMonths.length];
                    }
                });


                break;
            case 1:

                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return mDia[(int) value % mDia.length];

                    }
                });
                mArray=mDia;

                break;
        }


        xAxis.setAxisMaximum(data.getXMax() + 0.5f);


    }

    //metodo encargado de

    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        //  entries = getLineEntriesData(entries);

        LineDataSet set = new LineDataSet(entries, "Line");
        //set.setColor(Color.rgb(240, 238, 70));
        set.setColors(colores());
        set.setLineWidth(1f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        /*
         *
         * @return d
         * */
        return d;
    }
    //metodo encargado de tomar los datos para el grafico de linea
    /**
     *
     * @param entries
     * @return entries devuelve los datos que le ingresan procesados
     * */

    private ArrayList<Entry> getLineEntriesData(ArrayList<Entry> entries) {
        float total=0;
        for (int i = 0; i < mDatosDeUso.getGruposOrdenados().size(); i++)
        {
            try {
                total += mDatosDeUso.getGruposOrdenados().get(i).getInt("frecuencia");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 5; i++) {
            try {

                if(total>0)
                {
                    float prueba = ((mDatosDeUso.getGruposOrdenados().get(i).getInt("frecuencia") * 100) / total);
                    Log.e("valorPrueba",prueba+"");
                    entries.add(new Entry(i, prueba));}

                else
                {    entries.add(new Entry(i, 0));}

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return entries;
    }

    /**
     * metodo encargado de devolver valores del grafico de barra
     * @return  d devuelve los datos
     * */

    private BarData generateBarData() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        ArrayList<String> valores=new ArrayList<>();


        entries = getBarEnteries(entries);

        BarDataSet set1 = new BarDataSet(entries,"");
        set1.setColors(getColors());
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.35f; // x2 dataset


        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }
    // metodo encargado de generar los valores para el barentry
    /**
     * @param entries
     * @return entries
     * */
    private ArrayList<BarEntry> getBarEnteries(ArrayList<BarEntry> entries) {
        int total=0;
        float[][] prueba=new float[5][2];

        Log.e("DibGrafEst_cantG",total+"");
        int size = mDatosDeUso.getGrupoConPuntaje().size();
        if(size>5)
            size=5;


        for(int j=0;j<size;j++){
            try {
                Log.e("Grupos", mDatosDeUso.getGrupoConPuntaje().get(j).getJSONObject("texto").getString(context.getString(R.string.str_idioma)));

                float intentos = mDatosDeUso.getGrupoConPuntaje().get(j).getJSONArray("juegos").getJSONObject(0).getInt("intentos");
                float aciertos = mDatosDeUso.getGrupoConPuntaje().get(j).getJSONArray("juegos").getJSONObject(0).getInt("aciertos");
                    float errores = intentos - aciertos;
                    prueba[j][0] = (aciertos * 100 / (intentos));
                    prueba[j][1] = (errores * 100 / (intentos));
                    Log.e("GrafEst_ValorAgreg", prueba[j][0] + "");


            }catch (JSONException e) {
                    JSONArray array=new JSONArray();
                    JSONObject aux=new JSONObject();
                    try {
                        aux.put("puntaje",0);
                        aux.put("intentos",0);
                        aux.put("aciertos",0);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        mDatosDeUso.getGrupoConPuntaje().get(j).put("juegos", array);

                    } catch (JSONException e1) {
                        //e1.printStackTrace();
                        Log.e("GrafEst_error","no pudo cargar valores en la barra");
                    }
            }

        }
        for(int i=0;i<size;i++)
        {
            float[] array =new float[] {prueba[i][0],prueba[i][1]};
            entries.add(new BarEntry(i+1,array));
        }


        return  entries;
    }
    public CombinedChart getCombinedChart(int tipo) {
        switch (tipo)
        {
            case 0:
                dibujarCombinedChart(mChart,tipo);
                return mChart;
            case 1:
                dibujarCombinedChart(bubbleChart,tipo);
                return bubbleChart;
        }
        return mChart;
    }
    /*
     * @return bd
     * */

    protected BubbleData generateBubbleData() {

        BubbleData bd = new BubbleData();
        BubbleData d=new BubbleData();

        BubbleDataSet set = new BubbleDataSet(setBubbleEntries(), "Bubble DataSet");
        set.setColors(colores());
        set.setValueTextSize(9f);
        set.setValueTextColor(Color.BLACK);
        set.setHighlightCircleWidth(2f);
        set.setDrawValues(true);
        BubbleDataSet dataSet=new BubbleDataSet(set.getValues(),"");
        for (int i=0;i <set.getEntryCount();i++){
            if(set.getYMin()<=0)
            dataSet.removeEntry(i);
        }
        bd.addDataSet(dataSet);
        return bd;
    }
    //este metodo se encarga de hacer el grafico burbuja con respecto al uso durante el dia
    /*
     * @return entries
     * */
    private ArrayList<BubbleEntry> setBubbleEntries()
    {
        ArrayList<BubbleEntry> entries = new ArrayList<BubbleEntry>();
        int[][] matriz = mDatosDeUso.getCantidadFrasesPorDia();
        Color[] color =new Color[3];
        if (mDatosDeUso.getCantidadFrasesPorDia().length > 0)
        {for (int index = 0; index < 7; index++) {
            float y = 0;

            for(int i=0;i<3;i++)
            {
                float total=matriz[index][0]+matriz[index][1]+matriz[index][2];
                if(total>0)
                {y=(matriz[index][i]*100)/total;

                    if(y>0)
                        entries.add(new BubbleEntry(index,i+1,y));
                }
                else
                {

                    entries.add(new BubbleEntry(index,0,0));
                    entries.add(new BubbleEntry(index,4,0));
                }
            }
        }
            entries.add(new BubbleEntry(8,0,0));
        }

        return  entries;
    }


    //Metodos relaciondos con el piechart
    /*
     *
     * */
    private void dibujarPieChar()
    {


        PieDataSet datas=new PieDataSet(devolverDatos(),"");
        datas.setColors(colores());


        PieData data1=new PieData(datas);
        data1.setValueTextSize(8);
        pieChart.setData(data1);
        pieChart.setNoDataTextColor(Color.BLACK);


    }

    //datos de entrada para el piechart
    public ArrayList<PieEntry> devolverDatos()
    {   float total=0;
        float total1=0;
        ArrayList<PieEntry> resultado=new ArrayList<>();
        for (int i = 0; i < mDatosDeUso.getGruposOrdenados().size(); i++)
        {
            try {
                total += mDatosDeUso.getGruposOrdenados().get(i).getInt("frecuencia");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(total>0)
        {
            int size = mDatosDeUso.getGruposOrdenados().size();
            if(size>5)
                size=5;
            for(int i=0;i<size;i++)
        {
            try {
                float prueba = ((mDatosDeUso.getGruposOrdenados().get(i).getInt("frecuencia") * 100) / total);
                total1 += mDatosDeUso.getGruposOrdenados().get(i).getInt("frecuencia");
                resultado.add(new PieEntry(prueba, mDatosDeUso.getGruposOrdenados().get(i).getJSONObject("texto").getString(mSharedPrefsDefault.getString(context.getString(R.string.str_idioma), "en"))));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        }
        float prueba1=(((total-total1)*100)/total);
        resultado.add(new PieEntry(prueba1,context.getString(R.string.strOthers)));
        return resultado;
    }

    // devuelvo el piechart
    public PieChart getPieChart()
    {
        // dibujarPieChar();
        return pieChart;
    }
    // metodos encargados generar los valores de las etiquetas en el eje de las x e y
    private void cargarGrupos(int size)
    {
        /*
        * metodos encargados generar los valores de las etiquetas en el eje de las x e y
         * */

        mMonths=new String[size];
        for(int i=0;i<size;i++)
        {
            try {
                if(i>0)
                {
                    mMonths[i] = mDatosDeUso.getGruposOrdenados().get(i - 1).getJSONObject("texto").getString(mSharedPrefsDefault.getString(context
                            .getString(R.string.str_idioma),"en"));
               }else
                {
                    mMonths[0]="";
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void cargarDias()
    {
        mDia=new String[8];
        mDia[0]="";
        mDia[1]=context.getString(R.string.ddomingo).substring(0,3);
        mDia[2]=context.getString(R.string.dlunes).substring(0,3);
        mDia[3]=context.getString(R.string.dmartes).substring(0,3);
        mDia[4]=context.getString(R.string.dmiercoles).substring(0,3);
        mDia[5]=context.getString(R.string.djueves).substring(0,3);
        mDia[6]=context.getString(R.string.dviernes).substring(0,3);
        mDia[7]=context.getString(R.string.dSaturday).substring(0,3);




    }

    /**
     *metodo encargado  de generar los colores de
     * @return  colors devuelve el color
     */
    private  ArrayList<Integer> colores(){

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        return colors;
    }
    /**
     * colores del barchar
     * @return  barchar
     * **/
    private int[] getColors() {
        int[] colors;
        colors = new int[]{ColorTemplate.VORDIPLOM_COLORS[0],ColorTemplate.VORDIPLOM_COLORS[4]};
        return colors;
    }
    //este metodo se encarga de generar las etiquetas en pantalla
    public void mostrarLegend(CombinedChart combinedChart,String[] mArray)
    {
        //obtengo el leegend
        Legend l=combinedChart.getLegend();
        //habilito la etiqueta
        l.setEnabled(true);
        l.setWordWrapEnabled(true);
        //posiciono la etiqueta
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        // Dibujo la etiqueta afuera
        l.setDrawInside(false);
        //seteo el formato de la fuente del legend
        l.setTextSize(11f);
        l.setTypeface(Typeface.SANS_SERIF);
        //armo un arreglo con las entradas posibles

        ArrayList<LegendEntry> entries=new ArrayList<>();
        if(l.getEntries().length>0){
            entries.add(l.getEntries()[0]);
            entries.add(l.getEntries()[1]);
            //seteo las entradas con los textos del label
            entries.set(0,l.getEntries()[0]).label=mArray[0];
            entries.set(1,l.getEntries()[1]).label=mArray[1];
            //vacio la etiqueta
            l.resetCustom();
            //agrego las entradas
            l.setCustom(entries);
        }


    }


}
