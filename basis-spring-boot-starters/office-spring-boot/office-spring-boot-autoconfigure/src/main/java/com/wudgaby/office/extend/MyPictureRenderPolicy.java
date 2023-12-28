package com.wudgaby.office.extend;

import com.deepoove.poi.exception.RenderException;
import com.deepoove.poi.policy.AbstractRenderPolicy;
import com.deepoove.poi.render.RenderContext;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.ooxml.POIXMLTypeLoader;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/12/10 0010 10:02
 * @Desc :
 */
public class    MyPictureRenderPolicy extends AbstractRenderPolicy<MyPictureRenderData> {
    public MyPictureRenderPolicy() {
    }

    @Override
    protected boolean validate(MyPictureRenderData data) {
        if (null == data) {
            return false;
        } else if (null == data.getPictureType()) {
            throw new RenderException("PictureRenderData must set picture type!");
        } else {
            return true;
        }
    }

    @Override
    public void doRender(RenderContext<MyPictureRenderData> context) throws Exception {
        //com.deepoove.poi.policy.PictureRenderPolicy.Helper.renderPicture(context.getRun(), (PictureRenderData)context.getData());
        Helper.renderPicture(context.getRun(), context.getData());
    }

    @Override
    protected void afterRender(RenderContext<MyPictureRenderData> context) {
        this.clearPlaceholder(context, false);
    }

    @Override
    protected void reThrowException(RenderContext<MyPictureRenderData> context, Exception e) {
        this.logger.info("Render picture " + context.getEleTemplate() + " error: {}", e.getMessage());
        context.getRun().setText((context.getData()).getAltMeta(), 0);
    }

    public static class Helper {
        public Helper() {
        }

        public static void renderPicture(XWPFRun run, MyPictureRenderData picture) throws Exception {
            if (null == picture.getImage()) {
                throw new IllegalStateException("Can't get input data from picture!");
            } else {
                InputStream stream = new ByteArrayInputStream(picture.getImage());
                Throwable var3 = null;
                int suggestFileType = picture.getPictureType().type();

                //int picWidth = Units.toEMU(picture.getWidth());
                //int picHeight = Units.toEMU(1);

                String relationId = run.getDocument().addPictureData(stream, suggestFileType);
                long width = Units.toEMU(picture.getWidth());
                long height = Units.toEMU(picture.getHeight());
                CTDrawing drawing = run.getCTR().addNewDrawing();
                String xml = "<wp:anchor allowOverlap=\"1\" layoutInCell=\"1\" locked=\"0\" behindDoc=\"0\" " +
                        " relativeHeight=\"0\" simplePos=\"0\" distR=\"0\" distL=\"0\" distB=\"0\" distT=\"0\" " +
                        " xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\"" +
                        " xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\"" +
                        " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"  >" +
                        "<wp:simplePos y=\"" + 0 + "\" x=\" "+ 0 +" \"/>" +
                        //"<wp:simplePos y=\"" + picHeight + "\" x=\" "+ picWidth +" \"/>" +
                        "<wp:positionH relativeFrom=\"page\">" +
                        "<wp:align>right</wp:align>" +
                        //"<wp:posOffset>" + picWidth + "</wp:posOffset>" +
                        "</wp:positionH>" +
                        "<wp:positionV relativeFrom=\"line\">" +
                        "<wp:align>inside</wp:align>" +
                        //"<wp:posOffset>" + picHeight + "</wp:posOffset>" +
                        "</wp:positionV>" +
                        "<wp:extent cy=\""+height+"\" cx=\""+width+"\"/>" +
                        "<wp:effectExtent b=\"0\" r=\"0\" t=\"0\" l=\"0\"/>" +
                        "<wp:wrapTopAndBottom/>" +
                        "<wp:docPr descr=\"Picture Alt\" name=\"Picture Hit\" id=\"0\"/>" +
                        "<wp:cNvGraphicFramePr>" +
                        "<a:graphicFrameLocks noChangeAspect=\"true\" xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" />" +
                        "</wp:cNvGraphicFramePr>" +
                        "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" +
                        "<a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\" xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" +
                        "<pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                        "<pic:nvPicPr>" +
                        "<pic:cNvPr name=\"Picture Hit\" id=\"1\"/>" +
                        "<pic:cNvPicPr/>" +
                        "</pic:nvPicPr>" +
                        "<pic:blipFill>" +
                        "<a:blip r:embed=\""+relationId+"\"/>" +
                        "<a:stretch>" +
                        "<a:fillRect/>" +
                        "</a:stretch>" +
                        "</pic:blipFill>" +
                        "<pic:spPr>" +
                        "<a:xfrm>" +
                        "<a:off y=\"0\" x=\"0\"/>" +
                        "<a:ext cy=\""+height+"\" cx=\""+width+"\"/>" +
                        "</a:xfrm>" +
                        "<a:prstGeom prst=\"rect\">" +
                        "<a:avLst/>" +
                        "</a:prstGeom>" +
                        "</pic:spPr>" +
                        "</pic:pic>" +
                        "</a:graphicData>" +
                        "</a:graphic>" +
                        "<wp14:sizeRelH relativeFrom=\"margin\">" +
                        "<wp14:pctWidth>0</wp14:pctWidth>" +
                        "</wp14:sizeRelH>" +
                        "<wp14:sizeRelV relativeFrom=\"margin\">" +
                        "<wp14:pctHeight>0</wp14:pctHeight>" +
                        "</wp14:sizeRelV>" +
                        "<wp:wrapNone/>" +
                        "</wp:anchor>";
                drawing.set(XmlToken.Factory.parse(xml, POIXMLTypeLoader.DEFAULT_XML_OPTIONS));
                CTPicture pic = getCTPictures(drawing).get(0);
                XWPFPicture xwpfPicture = new XWPFPicture(pic, run);
                run.getEmbeddedPictures().add(xwpfPicture);
            }
        }

        public static List<CTPicture> getCTPictures(XmlObject o) {
            List<CTPicture> pictures = new ArrayList<>();
            XmlObject[] picts = o.selectPath("declare namespace pic='"
                    + CTPicture.type.getName().getNamespaceURI() + "' .//pic:pic");
            for (XmlObject pict : picts) {
                if (pict instanceof XmlAnyTypeImpl) {
                    // Pesky XmlBeans bug - see Bugzilla #49934
                    try {
                        pict = CTPicture.Factory.parse(pict.toString(),
                                POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
                    } catch (XmlException e) {
                        throw new POIXMLException(e);
                    }
                }
                if (pict instanceof CTPicture) {
                    pictures.add((CTPicture) pict);
                }
            }
            return pictures;
        }
    }

}
