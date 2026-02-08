import base64
import os
import json
import re

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import anthropic

app = FastAPI(title="SmartFixer API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


class DiagnoseRequest(BaseModel):
    description: str
    image_base64: str | None = None
    image_media_type: str | None = None


class DiagnoseResponse(BaseModel):
    title: str
    difficulty: str
    estimated_time: str
    tools: list[str]
    steps: list[str]
    category: str = "general"


SYSTEM_PROMPT = """\
You are a home repair diagnostic assistant. Given a description (and optionally a photo) \
of a home repair issue, provide a structured diagnosis. You MUST respond with valid JSON only, \
no other text. Use this exact format:
{
  "title": "Short title of the issue",
  "difficulty": "Easy|Medium|Hard",
  "estimated_time": "e.g. 30-45 minutes",
  "tools": ["tool1", "tool2"],
  "steps": ["Step 1 description", "Step 2 description"],
  "category": "plumbing|electrical|hvac|roofing|general|locksmith"
}
Provide practical, safe, actionable advice. Include 4-8 steps and 2-6 tools as appropriate. \
The category should match the type of repair professional who would handle this issue.\
"""


@app.get("/health")
async def health():
    return {"status": "ok"}


@app.post("/diagnose", response_model=DiagnoseResponse)
async def diagnose(request: DiagnoseRequest):
    api_key = os.environ.get("ANTHROPIC_API_KEY")
    if not api_key:
        raise HTTPException(status_code=500, detail="ANTHROPIC_API_KEY not configured")

    client = anthropic.Anthropic(api_key=api_key)

    # Build message content - text only or multimodal with image
    if request.image_base64 and request.image_media_type:
        content = [
            {
                "type": "image",
                "source": {
                    "type": "base64",
                    "media_type": request.image_media_type,
                    "data": request.image_base64,
                },
            },
            {
                "type": "text",
                "text": f"Diagnose this home repair issue: {request.description}",
            },
        ]
    else:
        content = f"Diagnose this home repair issue: {request.description}"

    try:
        message = client.messages.create(
            model="claude-sonnet-4-5-20250929",
            max_tokens=1024,
            system=SYSTEM_PROMPT,
            messages=[{"role": "user", "content": content}],
        )

        response_text = message.content[0].text
        # Strip markdown code fences if present
        match = re.search(r"```(?:json)?\s*(.*?)\s*```", response_text, re.DOTALL)
        if match:
            response_text = match.group(1)
        result = json.loads(response_text)

        return DiagnoseResponse(**result)
    except json.JSONDecodeError:
        raise HTTPException(status_code=502, detail=f"Failed to parse AI response: {response_text[:500]}")
    except anthropic.APIError as e:
        raise HTTPException(status_code=502, detail=f"AI service error: {e}")
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Unexpected error: {e}")
