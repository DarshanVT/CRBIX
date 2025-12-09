package paymentGateway;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart implements Serializable {
    private Map<String,Integer> items = new LinkedHashMap<>(); // itemId -> qty
    private Map<String,Item> catalog = new LinkedHashMap<>();

    
    private static final BigDecimal GST_RATE = new BigDecimal("0.18");

    public void setCatalog(Map<String,Item> catalog){ this.catalog = catalog; }
    public Map<String,Item> getCatalog(){ return Collections.unmodifiableMap(catalog); }

    public void addItem(String id, int qty){
        if(qty <= 0) return;
        items.put(id, items.getOrDefault(id, 0) + qty);
    }
    public void updateItem(String id, int qty){
        if(qty <= 0) items.remove(id);
        else items.put(id, qty);
    }
    public void removeItem(String id){ items.remove(id); }
    public Map<String,Integer> getItems(){ return Collections.unmodifiableMap(items); }
    public boolean isEmpty(){ return items.isEmpty(); }

    public BigDecimal getSubtotal(){
        BigDecimal subtotal = BigDecimal.ZERO;
        for(Map.Entry<String,Integer> e : items.entrySet()){
            Item it = catalog.get(e.getKey());
            if(it == null) continue;
            subtotal = subtotal.add(it.getPrice().multiply(new BigDecimal(e.getValue())));
        }
        return subtotal.setScale(2, RoundingMode.HALF_UP);
    }
    public BigDecimal getGst(){
        BigDecimal gst = getSubtotal().multiply(GST_RATE);
        return gst.setScale(2, RoundingMode.HALF_UP);
    }
    public BigDecimal getCgst(){
        return getGst().divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
    }
    public BigDecimal getSgst(){
        return getCgst();
    }
    public BigDecimal getTotal(){
        return getSubtotal().add(getGst()).setScale(2, RoundingMode.HALF_UP);
    }
}
