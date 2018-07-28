package cn.colintree.aix.SwipeRefresh;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.ListView;
import com.google.appinventor.components.runtime.VerticalScrollArrangement;
import com.google.appinventor.components.runtime.util.YailList;

import android.content.res.Resources;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.R.color;

@DesignerComponent(version = SwipeRefresh.VERSION,
    description = "by ColinTree at http://aix.colintree.cn",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "aiwebres/icon.png")

@SimpleObject(external = true)
@UsesLibraries(libraries = "support-v4.aar")
public class SwipeRefresh extends AndroidNonvisibleComponent implements Component {

    public static final int VERSION = 2;
    
    private static final String LOG_TAG = "SwipeRefresh";
    
    private ComponentContainer container;
    private Resources res;
    
    private SwipeRefreshLayout srl;

    private boolean enabled = true;
    private boolean nestedScrollingEnabled = true;
    private boolean large = false;
    private boolean scale = true;
    private int dragStart = 0;
    private int dragEnd = 150;
    private int backgroundColor = 0xFFFAFAFA;
    private YailList colorList;
    
    public SwipeRefresh(ComponentContainer container) {
        super(container.$form());
        Log.d(LOG_TAG, "SwipeRefresh Created");
        
        this.container = container;
        res = container.$context().getResources();
        
        ColorList(YailList.makeList(new Object[]{
            _Color_holo_blue_bright(),
            _Color_holo_green_light(),
            _Color_holo_orange_light(),
            _Color_holo_red_light()
        }));
    }

    @SimpleEvent
    public void Refresh() {
        EventDispatcher.dispatchEvent(this, "Refresh");
    }

    @SimpleFunction
    public void CancelRefreshing() {
        if (srl != null) {
            Refreshing(false);
        }
    }
    
    @SimpleProperty
    public void Refreshing(boolean refreshing) {
        if (srl != null) {
            srl.setRefreshing(refreshing);
        }
    }
    @SimpleProperty
    public boolean Refreshing() {
        if (srl != null) {
            return srl.isRefreshing();
        }
        return false;
    }
    
    @SimpleProperty
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "True")
    public void Enabled(boolean enabled) {
        this.enabled = enabled;
        if (srl != null) {
            srl.setEnabled(enabled);
        }
    }
    @SimpleProperty
    public boolean Enabled() {
        return enabled;
    }
    
    @SimpleProperty
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "True")
    public void NestedScrollingEnabled(boolean enabled) {
        this.nestedScrollingEnabled = enabled;
        if (srl != null) {
            srl.setNestedScrollingEnabled(enabled);
        }
    }
    @SimpleProperty
    public boolean NestedScrollingEnabled() {
        if (srl != null) {
            return srl.isNestedScrollingEnabled();
        }
        return nestedScrollingEnabled;
    }

    @SimpleProperty
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
    public void SizeLarge(boolean large) {
        this.large = large;
        if (srl != null) {
            srl.setSize(large ? SwipeRefreshLayout.LARGE : SwipeRefreshLayout.DEFAULT);
        }
    }
    @SimpleProperty
    public boolean SizeLarge() {
        return large;
    }

    @SimpleProperty
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "True")
    public void DragScale(boolean scale) {
        this.scale = scale;
        if (srl != null) {
            srl.setProgressViewOffset(DragScale(), DragStart(), DragStart());
        }
    }
    @SimpleProperty
    public boolean DragScale() {
        return scale;
    }

    @SimpleProperty
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue = "0")
    public void DragStart(int dragStart) {
        this.dragStart = dragStart;
        if (srl != null) {
            srl.setProgressViewOffset(DragScale(), DragStart(), DragStart());
        }
    }
    @SimpleProperty
    public int DragStart() {
        return dragStart;
    }

    @SimpleProperty
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue = "150")
    public void DragEnd(int dragEnd) {
        this.dragEnd = dragEnd;
        if (srl != null) {
            srl.setProgressViewOffset(DragScale(), DragStart(), DragEnd());
        }
    }
    @SimpleProperty
    public int DragEnd() {
        return dragEnd;
    }

    @SimpleProperty(description = "have to use the color that provided here")
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = Component.DEFAULT_VALUE_COLOR_DEFAULT)
    public void BackgroundColor(int color) {
        if(color == Component.COLOR_DEFAULT) {
            color = 0xFFFAFAFA;
        }
        this.backgroundColor = color;
        if (srl != null) {
            srl.setProgressBackgroundColorSchemeColor(color);
        }
    }
    @SimpleProperty
    public int BackgroundColor() {
        return backgroundColor;
    }

    @SimpleProperty
    public void ColorList(YailList list) {
        if(list == null) {
            return;
        }
        if (srl != null) {
            this.colorList = list;
            int[] color = new int[list.size()];
            for(int i = list.size()-1; i >= 0; i--) {  //avoid calling size() for many times
                color[i] = Integer.parseInt(list.getString(i));
            }
            srl.setColorSchemeColors(color);
        }
    }
    @SimpleProperty
    public YailList ColorList() {
        return colorList;
    }

    @SimpleFunction(description = "Vertical Scroll Arrangement allowed only")
    public void RegisterArrangement(VerticalScrollArrangement arrangement) {
        register(arrangement);
    }
    @SimpleFunction(description = "Vertical Scroll Arrangement allowed only")
    public void RegisterListView(ListView listView) {
        register(listView);
    }

    private void register(AndroidViewComponent component) {
        if (srl != null) {
            return;
        }
        srl = new SwipeRefreshLayout(container.$context());

        Enabled(Enabled());
        NestedScrollingEnabled(NestedScrollingEnabled());
        DragScale(DragScale());
        DragStart(DragStart());
        DragEnd(DragEnd());
        SizeLarge(SizeLarge());
        BackgroundColor(BackgroundColor());
        ColorList(ColorList());

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh();
            }
        });

        View child = component.getView();
        ViewGroup vg = (ViewGroup) child.getParent();
        if (vg.getChildCount() <= 0) {
            return;
            //check, though it is impossible
        }
        vg.addView(srl, vg.indexOfChild(child));
        vg.removeView(child);
        srl.addView(child);
    }

    @SimpleFunction
    public int _Color_holo_blue_bright() {
        return res.getColor(color.holo_blue_bright);
    }
    @SimpleFunction
    public int _Color_holo_blue_dark() {
        return res.getColor(color.holo_blue_dark);
    }
    @SimpleFunction
    public int _Color_holo_blue_light() {
        return res.getColor(color.holo_blue_light);
    }
    @SimpleFunction
    public int _Color_holo_green_dark() {
        return res.getColor(color.holo_green_dark);
    }
    @SimpleFunction
    public int _Color_holo_green_light() {
        return res.getColor(color.holo_green_light);
    }
    @SimpleFunction
    public int _Color_holo_orange_dark() {
        return res.getColor(color.holo_orange_dark);
    }
    @SimpleFunction
    public int _Color_holo_orange_light() {
        return res.getColor(color.holo_orange_light);
    }
    @SimpleFunction
    public int _Color_holo_purple() {
        return res.getColor(color.holo_purple);
    }
    @SimpleFunction
    public int _Color_holo_red_dark() {
        return res.getColor(color.holo_red_dark);
    }
    @SimpleFunction
    public int _Color_holo_red_light() {
        return res.getColor(color.holo_red_light);
    }
}